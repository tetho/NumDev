import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from './auth.service';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

describe('AuthService', () => {
    let service: AuthService;
    let httpMock: HttpTestingController;

    const baseUrl = 'api/auth';

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule],
            providers: [AuthService]
        });

        service = TestBed.inject(AuthService);
        httpMock = TestBed.inject(HttpTestingController);
    });

    afterEach(() => {
        httpMock.verify();
    });

    describe('register', () => {
        it('should send a POST request to register endpoint', () => {
            const registerRequest: RegisterRequest = {
                firstName: 'Admin',
                lastName: 'Admin',
                email: 'yoga@studio.com',
                password: 'test!1234'
            };

            service.register(registerRequest).subscribe();

            const req = httpMock.expectOne(`${baseUrl}/register`);
            expect(req.request.method).toBe('POST');
            expect(req.request.body).toEqual(registerRequest);
            req.flush({});
        });
    });

    describe('login', () => {
        it('should send a POST request to login endpoint and return session information', () => {
            const loginRequest: LoginRequest = {
                email: 'yoga@studio.com',
                password: 'test!1234'
            };

            const mockSessionInformation: SessionInformation = {
                token: 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5b2dhQHN0dWRpby5jb20iLCJpYXQiOjE3MjU3NzMxMzAsImV4cCI6MTcyNTg1OTUzMH0.yBUMX7DpcPDo2IFbLB5x5nefuULmdFCU57x-kQ59IZKR9b-xMf7kV0oOKVBK-lJ2mDgwnsrELHvbHD-iRQtm5w',
                type: 'Bearer',
                id: 1,
                username: 'yoga@studio.com',
                firstName: 'Admin',
                lastName: 'Admin',
                admin: true
            };

            service.login(loginRequest).subscribe((sessionInfo) => {
                expect(sessionInfo).toEqual(mockSessionInformation);
            });

            const req = httpMock.expectOne(`${baseUrl}/login`);
            expect(req.request.method).toBe('POST');
            expect(req.request.body).toEqual(loginRequest);
            req.flush(mockSessionInformation);
        });
    });
});
