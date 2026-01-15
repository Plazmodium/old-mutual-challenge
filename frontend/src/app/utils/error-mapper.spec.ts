import { HttpErrorResponse } from '@angular/common/http';
import { describe, it, expect } from 'vitest';
import { mapHttpError } from './error-mapper';

describe('ErrorMapper', () => {
  it('should map 404 error correctly', () => {
    const error = new HttpErrorResponse({ status: 404, statusText: 'Not Found' });
    const result = mapHttpError(error);
    expect(result.message).toContain('not found (404)');
    expect(result.code).toBe(404);
  });

  it('should map 502 error correctly', () => {
    const error = new HttpErrorResponse({ status: 502, statusText: 'Bad Gateway' });
    const result = mapHttpError(error);
    expect(result.message).toContain('Bad Gateway (502)');
    expect(result.code).toBe(502);
  });

  it('should map 503 error correctly', () => {
    const error = new HttpErrorResponse({ status: 503, statusText: 'Service Unavailable' });
    const result = mapHttpError(error);
    expect(result.message).toContain('Service Unavailable (503)');
    expect(result.code).toBe(503);
  });

  it('should map 504 error correctly', () => {
    const error = new HttpErrorResponse({ status: 504, statusText: 'Gateway Timeout' });
    const result = mapHttpError(error);
    expect(result.message).toContain('Gateway Timeout (504)');
    expect(result.code).toBe(504);
  });

  it('should map 0 error correctly', () => {
    const error = new HttpErrorResponse({ status: 0 });
    const result = mapHttpError(error);
    expect(result.message).toContain('check your internet connection');
    expect(result.code).toBe(0);
  });

  it('should use error message from body if available for unknown codes', () => {
    const customMessage = 'Custom API error';
    const error = new HttpErrorResponse({
      status: 400,
      error: { message: customMessage }
    });
    const result = mapHttpError(error);
    expect(result.message).toBe(customMessage);
    expect(result.code).toBe(400);
  });

  it('should return default message for non-HttpErrorResponse', () => {
    const result = mapHttpError(new Error('Generic error'));
    expect(result.message).toContain('unexpected error');
    expect(result.code).toBeNull();
  });
});
