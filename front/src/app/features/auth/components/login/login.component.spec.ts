import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';

import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { SessionService } from 'src/app/services/session.service';
import { of, throwError } from 'rxjs';
import { Router } from '@angular/router';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let sessionService: SessionService;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule],
      providers: [
        AuthService,
        SessionService
      ]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  describe('unit', () => {
    it('should create', () => {
      expect(component).toBeTruthy();
    });
  });

  describe('unit', () => {
    it('should have a valid email', () => {
      const emailControl = component.form.get('email');

      emailControl?.setValue('');
      expect(emailControl?.valid).toBeFalsy();
      expect(emailControl?.hasError('required')).toBeTruthy();

      emailControl?.setValue('invalidEmail');
      expect(emailControl?.valid).toBeFalsy();
      expect(emailControl?.hasError('email')).toBeTruthy();

      emailControl?.setValue('user@example.com');
      expect(emailControl?.valid).toBeTruthy();
    });
  });

  describe('unit', () => {
    it('should have a valid password', () => {
      const passwordControl = component.form.get('password');

      passwordControl?.setValue('');
      expect(passwordControl?.valid).toBeFalsy();
      expect(passwordControl?.hasError('required')).toBeTruthy();

      passwordControl?.setValue('password');
      expect(passwordControl?.valid).toBeTruthy();
    });
  });

  describe('unit', () => {
    it('should display an error if a mandatory field is empty', () => {
      const emailControl = component.form.get('email');
      const passwordControl = component.form.get('password');

      emailControl?.setValue('');
      emailControl?.markAsTouched();
      fixture.detectChanges();

      const emailField = fixture.nativeElement.querySelector('input[formControlName="email"]');
      expect(emailField.classList).toContain('ng-invalid');

      passwordControl?.setValue('');
      passwordControl?.markAsTouched();
      fixture.detectChanges();

      const passwordField = fixture.nativeElement.querySelector('input[formControlName="password"]');
      expect(passwordField.classList).toContain('ng-invalid');
    });
  });

  describe('integration', () => {
    it('should manage errors on bad login or password', () => {
      component.form.get('email')?.setValue('badLogin');
      component.form.get('password')?.setValue('badPassword');

      jest.spyOn(authService, 'login').mockReturnValue(throwError(() => new Error('Login failed')));

      component.submit();
      fixture.detectChanges();

      const errorMessage = fixture.nativeElement.querySelector('.error');
      expect(errorMessage).toBeTruthy();
      expect(errorMessage.textContent).toContain('An error occurred');
    });
  });

  describe('integration', () => {
    it('should log in', () => {
      component.form.get('email')?.setValue('yoga@studio.com');
      component.form.get('password')?.setValue('test!1234');

      const mockSessionInformation: SessionInformation = {
        token: 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5b2dhQHN0dWRpby5jb20iLCJpYXQiOjE3MjU3NzMxMzAsImV4cCI6MTcyNTg1OTUzMH0.yBUMX7DpcPDo2IFbLB5x5nefuULmdFCU57x-kQ59IZKR9b-xMf7kV0oOKVBK-lJ2mDgwnsrELHvbHD-iRQtm5w',
        type: 'Bearer',
        id: 1,
        username: 'yoga@studio.com',
        firstName: 'Admin',
        lastName: 'Admin',
        admin: true
      };

      jest.spyOn(authService, 'login').mockReturnValue(of(mockSessionInformation));
      jest.spyOn(sessionService, 'logIn').mockImplementation(() => of(mockSessionInformation));
      jest.spyOn(router, 'navigate').mockResolvedValue(true);

      component.submit();
      fixture.detectChanges();

      expect(sessionService.logIn).toHaveBeenCalledWith(mockSessionInformation);

      expect(router.navigate).toHaveBeenCalledWith(['/sessions']);
    });
  });
});
