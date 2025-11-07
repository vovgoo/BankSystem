import axios, { type AxiosInstance, type AxiosRequestConfig, type AxiosResponse } from 'axios';
import { ApiInterceptors } from './interceptors';
import type { Toaster } from './transaction-notifications';

export interface ApiClientConfig {
  baseURL?: string;
  timeout?: number;
  headers?: Record<string, string>;
  getToaster?: () => Toaster;
}

export class ApiClient {
  private axiosInstance: AxiosInstance;

  constructor(config: ApiClientConfig = {}) {
    const defaultBaseURL = import.meta.env.VITE_API_URL || '';
    this.axiosInstance = axios.create({
      baseURL: config.baseURL ?? defaultBaseURL,
      timeout: config.timeout ?? 30000,
      headers: {
        'Content-Type': 'application/json',
        ...config.headers,
      },
    });

    ApiInterceptors.setup(this.axiosInstance, {
      getToaster: config.getToaster,
    });
  }

  async get<T>(url: string, config?: AxiosRequestConfig): Promise<T> {
    const response: AxiosResponse<T> = await this.axiosInstance.get(url, config);
    return response.data;
  }

  async post<T>(url: string, data?: unknown, config?: AxiosRequestConfig): Promise<T> {
    const response: AxiosResponse<T> = await this.axiosInstance.post(url, data, config);
    return response.data;
  }

  async put<T>(url: string, data?: unknown, config?: AxiosRequestConfig): Promise<T> {
    const response: AxiosResponse<T> = await this.axiosInstance.put(url, data, config);
    return response.data;
  }

  async patch<T>(url: string, data?: unknown, config?: AxiosRequestConfig): Promise<T> {
    const response: AxiosResponse<T> = await this.axiosInstance.patch(url, data, config);
    return response.data;
  }

  async delete<T>(url: string, config?: AxiosRequestConfig): Promise<T> {
    const response: AxiosResponse<T> = await this.axiosInstance.delete(url, config);
    return response.data;
  }

  getAxiosInstance(): AxiosInstance {
    return this.axiosInstance;
  }
}

let globalToaster: (() => Toaster) | undefined;

export function initializeWithToaster(getToaster: () => Toaster): void {
  globalToaster = getToaster;
  const newInstance = axios.create({
    baseURL: apiClient.getAxiosInstance().defaults.baseURL,
    timeout: apiClient.getAxiosInstance().defaults.timeout,
    headers: apiClient.getAxiosInstance().defaults.headers,
  });

  ApiInterceptors.setup(newInstance, { getToaster });

  Object.assign(apiClient, { axiosInstance: newInstance } as Partial<ApiClient>);
}

export const apiClient = new ApiClient(globalToaster ? { getToaster: globalToaster } : {});
