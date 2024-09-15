import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import { SessionApiService } from '../../services/session-api.service';
import { Session } from '../../interfaces/session.interface';
import { of } from 'rxjs';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { TeacherService } from 'src/app/services/teacher.service';
import { Teacher } from 'src/app/interfaces/teacher.interface';


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let service: SessionService;
  let sessionApiService: SessionApiService;
  let teacherService: TeacherService;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }

  const mockSession: Session = {
    name: 'session 1',
    date: new Date('2012-01-01'),
    teacher_id: 5,
    description: 'my description',
    users: [2, 4, 6, 8]
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        MatCardModule,
        MatIconModule
      ],
      declarations: [DetailComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        SessionApiService,
        TeacherService
      ],
    })
      .compileComponents();
    service = TestBed.inject(SessionService);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    sessionApiService = TestBed.inject(SessionApiService);
    teacherService = TestBed.inject(TeacherService);
    fixture.detectChanges();
  });

  describe('unit', () => {
    it('should create', () => {
      expect(component).toBeTruthy();
    });
  });

  describe('unit', () => {
    it('should go back when back is called', () => {
      const spy = jest.spyOn(window.history, 'back');
      component.back();
      expect(spy).toHaveBeenCalled();
    });
  });

  describe('integration', () => {
    it('should display the session information', () => {
      jest.spyOn(sessionApiService, 'detail').mockReturnValue(of(mockSession));

      component.ngOnInit();

      fixture.detectChanges();

      const sessionName = fixture.nativeElement.querySelector('mat-card-title h1');
      const sessionDate = fixture.nativeElement.querySelector('mat-card-content .my2 div:nth-of-type(2) .ml1');
      const sessionDescription = fixture.nativeElement.querySelector('mat-card-content .description');

      expect(sessionName).toBeTruthy();
      expect(sessionName.textContent).toContain('Session 1');

      expect(sessionDate).toBeTruthy();
      expect(sessionDate.textContent).toContain('January 1, 2012');

      expect(sessionDescription).toBeTruthy();
      expect(sessionDescription.textContent).toContain('my description');
    });
  });

  describe('integration', () => {
    it('should display the number of attendees', () => {
      jest.spyOn(sessionApiService, 'detail').mockReturnValue(of(mockSession));

      component.ngOnInit();
      fixture.detectChanges();

      const attendeesInfo = fixture.nativeElement.querySelector('.my2 .ml1');

      expect(attendeesInfo).toBeTruthy();
      expect(attendeesInfo.textContent).toContain('4 attendees');
    });
  });

  describe('integration', () => {
    it('should display the teacher information', () => {
      const mockTeacher: Teacher = {
        id: 1,
        firstName: 'Margot',
        lastName: 'Delahaye',
        createdAt: new Date(),
        updatedAt: new Date()
      };

      jest.spyOn(sessionApiService, 'detail').mockReturnValue(of(mockSession));
      jest.spyOn(teacherService, 'detail').mockReturnValue(of(mockTeacher));

      component.ngOnInit();
      fixture.detectChanges();

      const teacherInfo = fixture.nativeElement.querySelector('mat-card-subtitle .ml1');

      expect(teacherInfo).toBeTruthy();
      expect(teacherInfo.textContent).toContain('Margot DELAHAYE');
    });
  });

  describe('integration', () => {
    it('should display the delete button if the user is an admin', () => {
      component.isAdmin = true;

      jest.spyOn(sessionApiService, 'detail').mockReturnValue(of(mockSession));

      component.ngOnInit();
      fixture.detectChanges();

      const deleteButton = fixture.nativeElement.querySelector('button[mat-raised-button][color="warn"] mat-icon');

      expect(deleteButton).toBeTruthy();
      expect(deleteButton.textContent).toContain('delete');
    });
  });

  describe('integration', () => {
    it('should call delete when delete button is clicked', () => {
      component.isAdmin = true;

      jest.spyOn(sessionApiService, 'detail').mockReturnValue(of(mockSession));
      jest.spyOn(sessionApiService, 'delete').mockReturnValue(of(mockSession));

      component.ngOnInit();
      fixture.detectChanges();

      const deleteButton = fixture.nativeElement.querySelector('button[mat-raised-button][color="warn"] mat-icon');
      deleteButton.click();

      expect(sessionApiService.delete).toHaveBeenCalledWith(component.sessionId);
    });
  });

  describe('integration', () => {
    it('should display the participate button if the user is not an admin', () => {
      component.isAdmin = false;
      component.isParticipate = false;

      jest.spyOn(sessionApiService, 'detail').mockReturnValue(of(mockSession));

      component.ngOnInit();
      fixture.detectChanges();

      const participateButton = fixture.nativeElement.querySelector('button[mat-raised-button][color="primary"] mat-icon');

      expect(participateButton).toBeTruthy();
      expect(participateButton.textContent).toContain('person_add');
    });
  });

  describe('integration', () => {
    it('should call participate when participate button is clicked', () => {
      component.isAdmin = false;
      component.isParticipate = false;

      jest.spyOn(sessionApiService, 'detail').mockReturnValue(of(mockSession));
      jest.spyOn(sessionApiService, 'participate').mockReturnValue(of(void 0));

      component.ngOnInit();
      fixture.detectChanges();

      const participateButton = fixture.nativeElement.querySelector('button[mat-raised-button][color="primary"] mat-icon');
      participateButton.click();

      expect(sessionApiService.participate).toHaveBeenCalledWith(component.sessionId, component.userId);
    });
  });

  describe('integration', () => {
    it('should display the unparticipate button if the user is not an admin and is participating', () => {
      component.isAdmin = false;
      component.isParticipate = true;

      const mockSessionWithUser = {
        ...mockSession,
        users: [1, 2, 4, 6, 8]
      };

      jest.spyOn(sessionApiService, 'detail').mockReturnValue(of(mockSessionWithUser));

      component.ngOnInit();
      fixture.detectChanges();

      const unparticipateButton = fixture.nativeElement.querySelector('button[mat-raised-button][color="warn"] mat-icon');

      expect(unparticipateButton).toBeTruthy();
      expect(unparticipateButton.textContent).toContain('person_remove');
    });
  });

  describe('integration', () => {
    it('should call unparticipate when unparticipate button is clicked', () => {
      component.isAdmin = false;
      component.isParticipate = true;

      const mockSessionWithUser = {
        ...mockSession,
        users: [1, 2, 4, 6, 8]
      };

      jest.spyOn(sessionApiService, 'detail').mockReturnValue(of(mockSessionWithUser));
      jest.spyOn(sessionApiService, 'unParticipate').mockReturnValue(of(void 0));

      component.ngOnInit();
      fixture.detectChanges();

      const unparticipateButton = fixture.nativeElement.querySelector('button[mat-raised-button][color="warn"] mat-icon');
      unparticipateButton.click();

      expect(sessionApiService.unParticipate).toHaveBeenCalledWith(component.sessionId, component.userId);
    });
  });

});
