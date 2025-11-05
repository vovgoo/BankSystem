import type { AxiosInstance, InternalAxiosRequestConfig, AxiosResponse, AxiosError } from 'axios';
import { ApiErrorHandler } from './error-handler';
import {
  isTransactionRequest,
  showTransactionSuccess,
  showTransactionError,
} from './transaction-notifications';
import type { TransactionResponse } from '../types';

export interface InterceptorConfig {
  onRequest?: (config: InternalAxiosRequestConfig) => InternalAxiosRequestConfig;
  onRequestError?: (error: AxiosError) => Promise<never>;
  onResponse?: <T>(response: AxiosResponse<T>) => AxiosResponse<T>;
  onResponseError?: (error: AxiosError) => Promise<never>;

  getToaster?: () => import('./transaction-notifications').Toaster;
}

const requestMetadata = new WeakMap<InternalAxiosRequestConfig, { isTransaction?: boolean }>();

export class ApiInterceptors {
  static setup(instance: AxiosInstance, config?: InterceptorConfig): void {
    instance.interceptors.request.use(
      (requestConfig) => {
        const isTransaction = isTransactionRequest(requestConfig);
        requestMetadata.set(requestConfig, { isTransaction });

        return config?.onRequest ? config.onRequest(requestConfig) : requestConfig;
      },
      (error) => {
        return config?.onRequestError ? config.onRequestError(error) : Promise.reject(error);
      }
    );

    instance.interceptors.response.use(
      <T>(response: AxiosResponse<T>) => {
        const metadata = requestMetadata.get(response.config);
        const isTransaction = metadata?.isTransaction;
        const toaster = config?.getToaster?.();

        if (isTransaction && toaster) {
          showTransactionSuccess(toaster);
        }

        requestMetadata.delete(response.config);

        return config?.onResponse ? config.onResponse(response) : response;
      },
      (error: AxiosError<TransactionResponse>) => {
        const apiError = ApiErrorHandler.handle(error);

        if (import.meta.env.DEV) {
          console.error('API Error:', apiError);
        }

        const metadata = error.config ? requestMetadata.get(error.config) : undefined;
        const isTransaction = metadata?.isTransaction;
        const toaster = config?.getToaster?.();

        if (isTransaction && toaster) {
          showTransactionError(error, toaster);
        }

        if (error.config) {
          requestMetadata.delete(error.config);
        }

        return config?.onResponseError ? config.onResponseError(error) : Promise.reject(apiError);
      }
    );
  }
}
