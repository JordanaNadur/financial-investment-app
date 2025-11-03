import { Injectable, signal } from '@angular/core';

export type ToastType = 'success' | 'error' | 'info';

export interface Toast {
  id: number;
  message: string;
  type: ToastType;
  timeout?: number;
}

@Injectable({ providedIn: 'root' })
export class ToastService {
  private seq = 0;
  toasts = signal<Toast[]>([]);

  show(message: string, type: ToastType = 'info', timeout = 3000) {
    const id = ++this.seq;
    const toast: Toast = { id, message, type, timeout };
    this.toasts.update((arr) => [toast, ...arr]);
    if (timeout && timeout > 0) {
      setTimeout(() => this.dismiss(id), timeout);
    }
  }

  success(message: string, timeout = 3000) {
    this.show(message, 'success', timeout);
  }

  error(message: string, timeout = 4000) {
    this.show(message, 'error', timeout);
  }

  info(message: string, timeout = 3000) {
    this.show(message, 'info', timeout);
  }

  dismiss(id: number) {
    this.toasts.update((arr) => arr.filter((t) => t.id !== id));
  }
}
