import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, fakeAsync, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { Session } from '../../interfaces/session.interface';
import { of } from 'rxjs';
import { Router } from '@angular/router';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let sessionApiService: SessionApiService;
  let router: Router;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  }

  const mockMatSnackBar = {
    open: jest.fn()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({

      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        SessionApiService
      ],
      declarations: [FormComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    sessionApiService = TestBed.inject(SessionApiService);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  describe('unit', () => {
    it('should create', () => {
      expect(component).toBeTruthy();
    });
  });

  describe('unit', () => {
    it('should have a valid name', () => {
      const nameControl = component.sessionForm?.get('name');

      nameControl?.setValue('');
      expect(nameControl?.valid).toBeFalsy();

      nameControl?.setValue('session 1');
      expect(nameControl?.valid).toBeTruthy();
    });
  });

  describe('unit', () => {
    it('should have a valid date', () => {
      const dateControl = component.sessionForm?.get('date');

      dateControl?.setValue('');
      expect(dateControl?.valid).toBeFalsy();

      dateControl?.setValue('2012-01-01');
      expect(dateControl?.valid).toBeTruthy();
    });
  });

  describe('unit', () => {
    it('should have a valid teacher', () => {
      const teacherIdControl = component.sessionForm?.get('teacher_id');

      teacherIdControl?.setValue('');
      expect(teacherIdControl?.valid).toBeFalsy();

      teacherIdControl?.setValue('5');
      expect(teacherIdControl?.valid).toBeTruthy();
    });
  });

  describe('unit', () => {
    it('should have a valid description', () => {
      const descriptionControl = component.sessionForm?.get('description');

      descriptionControl?.setValue('');
      expect(descriptionControl?.valid).toBeFalsy();

      descriptionControl?.setValue('my description');
      expect(descriptionControl?.valid).toBeTruthy();
    });
  });

  describe('unit', () => {
    it('should display an error if a mandatory field is empty', () => {
      const nameControl = component.sessionForm?.get('name');
      const dateControl = component.sessionForm?.get('date');
      const teacherIdControl = component.sessionForm?.get('teacher_id');
      const descriptionControl = component.sessionForm?.get('description');

      nameControl?.setValue('');
      nameControl?.markAsTouched();
      fixture.detectChanges();

      const nameField = fixture.nativeElement.querySelector('input[formControlName="name"]');
      expect(nameField.classList).toContain('ng-invalid');

      dateControl?.setValue('');
      dateControl?.markAsTouched();
      fixture.detectChanges();

      const dateField = fixture.nativeElement.querySelector('input[formControlName="date"]');
      expect(dateField.classList).toContain('ng-invalid');

      teacherIdControl?.setValue('');
      teacherIdControl?.markAsTouched();
      fixture.detectChanges();

      const teacherIdField = fixture.nativeElement.querySelector('mat-select[formControlName="teacher_id"]');
      expect(teacherIdField.classList).toContain('ng-invalid');

      descriptionControl?.setValue('');
      descriptionControl?.markAsTouched();
      fixture.detectChanges();

      const descriptionField = fixture.nativeElement.querySelector('textarea[formControlName="description"]');
      expect(descriptionField.classList).toContain('ng-invalid');
    });
  });

  describe('integration', () => {
    it('should create a session', () => {
      component.sessionForm?.get('name')?.setValue('session 1');
      component.sessionForm?.get('date')?.setValue(new Date('2012-01-01'));
      component.sessionForm?.get('teacher_id')?.setValue(5);
      component.sessionForm?.get('description')?.setValue('my description');

      const mockSession: Session = {
        name: 'session 1',
        date: new Date('2012-01-01'),
        teacher_id: 5,
        description: 'my description',
        users: []
      }

      jest.spyOn(sessionApiService, 'create').mockReturnValue(of(mockSession));
      jest.spyOn(router, 'navigate').mockResolvedValue(true);

      component.submit();
      fixture.detectChanges();

      expect(sessionApiService.create).toHaveBeenCalledWith({
        name: mockSession.name,
        date: mockSession.date,
        teacher_id: mockSession.teacher_id,
        description: mockSession.description,
      });

      expect(router.navigate).toHaveBeenCalledWith(['sessions']);
    });
  });
});
