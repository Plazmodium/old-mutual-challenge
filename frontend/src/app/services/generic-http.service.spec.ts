import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { GenericHttpService } from './generic-http.service';

describe('GenericHttpService', () => {
  let service: GenericHttpService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [GenericHttpService]
    });
    service = TestBed.inject(GenericHttpService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should perform GET request', () => {
    const testData = { name: 'test' };
    service.get('/test').subscribe(data => {
      expect(data).toEqual(testData);
    });

    const req = httpMock.expectOne('/test');
    expect(req.request.method).toBe('GET');
    req.flush(testData);
  });

  it('should perform POST request', () => {
    const testData = { success: true };
    const body = { id: 1 };
    service.post('/test', body).subscribe(data => {
      expect(data).toEqual(testData);
    });

    const req = httpMock.expectOne('/test');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(body);
    req.flush(testData);
  });

  it('should perform PUT request', () => {
    const testData = { success: true };
    const body = { id: 1 };
    service.put('/test', body).subscribe(data => {
      expect(data).toEqual(testData);
    });

    const req = httpMock.expectOne('/test');
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(body);
    req.flush(testData);
  });

  it('should perform DELETE request', () => {
    const testData = { success: true };
    service.delete('/test').subscribe(data => {
      expect(data).toEqual(testData);
    });

    const req = httpMock.expectOne('/test');
    expect(req.request.method).toBe('DELETE');
    req.flush(testData);
  });
});
