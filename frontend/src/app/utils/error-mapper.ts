import { HttpErrorResponse } from '@angular/common/http';

export function mapHttpError(error: any): { message: string, code: number | null } {
  if (error instanceof HttpErrorResponse) {
    const code = error.status;
    switch (code) {
      case 404:
        return { message: 'The requested resource was not found (404).', code };
      case 502:
        return { message: 'Bad Gateway (502). The server received an invalid response from the upstream server.', code };
      case 503:
        return { message: 'Service Unavailable (503). The server is currently unable to handle the request.', code };
      case 504:
        return { message: 'Gateway Timeout (504). The server did not receive a timely response from the upstream server.', code };
      case 0:
        return { message: 'Cannot connect to the server. Please check your internet connection.', code };
      default:
        return { message: error.error?.message || `An unexpected error occurred (${code}). Please try again later.`, code };
    }
  }
  return { message: 'Failed to load data. Please try again later.', code: null };
}
