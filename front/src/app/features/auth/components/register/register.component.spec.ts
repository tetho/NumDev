import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';
import { of } from 'rxjs';
import { Router } from '@angular/router';
import { last } from 'cypress/types/lodash';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: AuthService;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        AuthService
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  describe('unit', () => {
    it('should create', () => {
      expect(component).toBeTruthy();
    });
  });

  describe('unit', () => {
    it('should have a valid first name', () => {
      const firstNameControl = component.form.get('firstName');

      firstNameControl?.setValue('');
      expect(firstNameControl?.valid).toBeFalsy();

      firstNameControl?.setValue('toto');
      expect(firstNameControl?.valid).toBeTruthy();
    });
  });

  describe('unit', () => {
    it('should have a valid last name', () => {
      const lastNameControl = component.form.get('lastName');

      lastNameControl?.setValue('');
      expect(lastNameControl?.valid).toBeFalsy();

      lastNameControl?.setValue('toto');
      expect(lastNameControl?.valid).toBeTruthy();
    });
  });

  describe('unit', () => {
    it('should have a valid email', () => {
      const emailControl = component.form.get('email');

      emailControl?.setValue('');
      expect(emailControl?.valid).toBeFalsy();

      emailControl?.setValue('invalidEmail');
      expect(emailControl?.valid).toBeFalsy();

      emailControl?.setValue('user@example.com');
      expect(emailControl?.valid).toBeTruthy();
    });
  });

  describe('unit', () => {
    it('should have a valid password', () => {
      const passwordControl = component.form.get('password');

      passwordControl?.setValue('');
      expect(passwordControl?.valid).toBeFalsy();

      passwordControl?.setValue('test!1234');
      expect(passwordControl?.valid).toBeTruthy();
    });
  });

  describe('unit', () => {
    it('should have an error if a mandatory field is empty', () => {
      const validValues = {
        firstName: 'toto',
        lastName: 'toto',
        email: 'toto3@toto.com',
        password: 'test!1234'
      };
      const invalidValues = {
        firstName: '',
        lastName: '',
        email: '',
        password: ''
      };

      for (const [field, _] of Object.entries(validValues)) {
        const formValues = { ...validValues };
        formValues[field as keyof typeof validValues] = invalidValues[field as keyof typeof validValues];

        component.form.patchValue(formValues);
        fixture.detectChanges();

        expect(component.form.valid).toBeFalsy();
      }

      component.form.patchValue(validValues);
      fixture.detectChanges();
      expect(component.form.valid).toBeTruthy();
    });
  });

  describe('integration', () => {
    it('should register', () => {
      component.form.get('firstName')?.setValue('toto');
      component.form.get('lastName')?.setValue('toto');
      component.form.get('email')?.setValue('toto3@toto.com');
      component.form.get('password')?.setValue('test!1234');

      const mockSuccessResponse: void = undefined;

      jest.spyOn(authService, 'register').mockReturnValue(of(mockSuccessResponse));
      jest.spyOn(router, 'navigate').mockResolvedValue(true);

      component.submit();
      fixture.detectChanges();

      expect(authService.register).toHaveBeenCalledWith({
        firstName: 'toto',
        lastName: 'toto',
        email: 'toto3@toto.com',
        password: 'test!1234'
      });

      expect(router.navigate).toHaveBeenCalledWith(['/login']);
    });
  });
});
