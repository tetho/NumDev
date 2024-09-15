import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { ListComponent } from './list.component';
import { SessionApiService } from '../../services/session-api.service';
import { of } from 'rxjs';
import { By } from '@angular/platform-browser';
import { Session } from '../../interfaces/session.interface';
import { DebugElement } from '@angular/core';

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;
  let sessionApiService: SessionApiService;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  }

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

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [HttpClientModule, MatCardModule, MatIconModule],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        SessionApiService
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    sessionApiService = TestBed.inject(SessionApiService);
    fixture.detectChanges();
  });

  describe('unit', () => {
    it('should create', () => {
      expect(component).toBeTruthy();
    });
  });

  describe('integration', () => {
    it('should display the list of sessions', () => {
      jest.spyOn(sessionApiService, 'all').mockReturnValue(of(mockSessions));

      fixture.detectChanges();

      fixture.whenStable().then(() => {
        fixture.detectChanges();

        const items = fixture.debugElement.queryAll(By.css('.items mat-card.item'));
        expect(items.length).toBe(mockSessions.length);

        mockSessions.forEach((session, index) => {
          const card = items[index];
          expect(card.nativeElement.querySelector('mat-card-title').textContent).toContain(session.name);
          expect(card.nativeElement.querySelector('mat-card-subtitle').textContent).toContain(`Session on ${session.date.toLocaleDateString()}`);
          expect(card.nativeElement.querySelector('p').textContent).toContain(session.description);
        });
      });

    });
  });

  describe('integration', () => {
    it('should display the create and detail buttons if the user is an admin', () => {
      mockSessionService.sessionInformation = { admin: true };

      jest.spyOn(sessionApiService, 'all').mockReturnValue(of(mockSessions));

      fixture.detectChanges();

      fixture.whenStable().then(() => {
        fixture.detectChanges();

        const createButton = fixture.nativeElement.querySelector('mat-card-header button[routerLink="create"]');
        expect(createButton).toBeTruthy();
        expect(createButton.textContent).toContain('Create');

        const detailButton = fixture.nativeElement.querySelector('mat-card-actions button[routerLink]');
        expect(detailButton).toBeTruthy();
        expect(detailButton.textContent).toContain('search');
      });
    });
  });
});
