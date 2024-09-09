import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  describe('unit', () => {
    it('should be created', () => {
      expect(service).toBeTruthy();
    });
  });

  describe('integration', () => {
    it('should log in', () => {
      const mockSessionInformation: SessionInformation = {
        token: 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5b2dhQHN0dWRpby5jb20iLCJpYXQiOjE3MjU3NzMxMzAsImV4cCI6MTcyNTg1OTUzMH0.yBUMX7DpcPDo2IFbLB5x5nefuULmdFCU57x-kQ59IZKR9b-xMf7kV0oOKVBK-lJ2mDgwnsrELHvbHD-iRQtm5w',
        type: 'Bearer',
        id: 1,
        username: 'yoga@studio.com',
        firstName: 'Admin',
        lastName: 'Admin',
        admin: true
      };
      service.logIn(mockSessionInformation);

      expect(service.isLogged).toBe(true);
      expect(service.sessionInformation).toEqual(mockSessionInformation);
    });
  });

  describe('integration', () => {
    it('should log out', () => {
      const mockSessionInformation: SessionInformation = {
        token: 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5b2dhQHN0dWRpby5jb20iLCJpYXQiOjE3MjU3NzMxMzAsImV4cCI6MTcyNTg1OTUzMH0.yBUMX7DpcPDo2IFbLB5x5nefuULmdFCU57x-kQ59IZKR9b-xMf7kV0oOKVBK-lJ2mDgwnsrELHvbHD-iRQtm5w',
        type: 'Bearer',
        id: 1,
        username: 'yoga@studio.com',
        firstName: 'Admin',
        lastName: 'Admin',
        admin: true
      };
      service.logIn(mockSessionInformation);

      service.logOut();

      expect(service.isLogged).toBe(false);
      expect(service.sessionInformation).toBeUndefined();
    });
  });
});
