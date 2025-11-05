import type { AxiosError } from 'axios';
import type { TransactionResponse } from '../types';

export interface ApiError {
  message: string;
  status?: number;
  data?: unknown;
}

export class ApiErrorHandler {
  static handle(error: unknown): ApiError {
    const axiosError = error as AxiosError<TransactionResponse>;

    if (axiosError.response) {
      const errorResponse = axiosError.response.data;

      return {
        message: errorResponse?.message || 'Произошла ошибка на сервере',
        status: axiosError.response.status,
        data: errorResponse,
      };
    }

    if (axiosError.request) {
      return {
        message: 'Не удалось получить ответ от сервера. Проверьте подключение к интернету',
        status: undefined,
        data: undefined,
      };
    }

    return {
      message: axiosError.message || 'Произошла неизвестная ошибка',
      status: undefined,
      data: undefined,
    };
  }

  static isNetworkError(error: unknown): boolean {
    const axiosError = error as AxiosError;
    return !axiosError.response && !!axiosError.request;
  }

  static isServerError(error: unknown): boolean {
    const axiosError = error as AxiosError;
    return !!axiosError.response && axiosError.response.status >= 500;
  }

  static isClientError(error: unknown): boolean {
    const axiosError = error as AxiosError;
    return (
      !!axiosError.response && axiosError.response.status >= 400 && axiosError.response.status < 500
    );
  }
}
