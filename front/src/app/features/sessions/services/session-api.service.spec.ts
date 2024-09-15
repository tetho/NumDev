import { HttpClientModule } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';

describe('SessionsService', () => {
  let service: SessionApiService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        HttpClientTestingModule
      ],
      providers: [
        SessionApiService
      ]
    });
    service = TestBed.inject(SessionApiService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('integration', () => {
    it('should return all sessions', () => {
      const mockSessions: Session[] = [
        {
          id: 1,
          name: 'Yoga Class',
          date: new Date('2012-01-01'),
          description: 'A relaxing yoga session.',
          teacher_id: 1,
          users: [1, 2, 3, 4]
        },
        {
          id: 2,
          name: 'Pilates Class',
          date: new Date('2012-01-02'),
          description: 'A strength-building Pilates session.',
          teacher_id: 2,
          users: [2, 4, 6, 8]
        }
      ];

      service.all().subscribe(sessions => {
        expect(sessions).toEqual(mockSessions);
      });

      const req = httpTestingController.expectOne('api/session');
      expect(req.request.method).toBe('GET');
      req.flush(mockSessions);
    });
  });

  describe('integration', () => {
    it('should create a session', () => {
      const mockSession: Session = {
        name: 'session 1',
        date: new Date('2012-01-01'),
        teacher_id: 5,
        description: 'my description',
        users: [2, 4, 6, 8]
      }

      service.create(mockSession).subscribe(session => {
        expect(session).toEqual(mockSession);
      });

      const req = httpTestingController.expectOne('api/session');
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(mockSession);
      req.flush(mockSession);
    });
  });

  describe('integration', () => {
    it('should update a session', () => {
      const mockSession: Session = {
        name: 'Pilates Class',
        date: new Date('2012-01-02'),
        description: 'A strength-building Pilates session.',
        teacher_id: 2,
        users: [2, 4, 6, 8]
      }

      service.update('1', mockSession).subscribe(session => {
        expect(session).toEqual(mockSession);
      });

      const req = httpTestingController.expectOne('api/session/1');
      expect(req.request.method).toBe('PUT');
      expect(req.request.body).toEqual(mockSession);
      req.flush(mockSession);
    });
  });

  describe('integration', () => {
    it('should delete a session', () => {
      service.delete('1').subscribe(response => {
        expect(response).toBeTruthy();
      });

      const req = httpTestingController.expectOne('api/session/1');
      expect(req.request.method).toBe('DELETE');
      req.flush({});
    });
  });
});
