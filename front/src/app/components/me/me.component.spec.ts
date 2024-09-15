import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';

import { MeComponent } from './me.component';
import { UserService } from 'src/app/services/user.service';
import { User } from 'src/app/interfaces/user.interface';
import { of } from 'rxjs';
import { By } from '@angular/platform-browser';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let userService: UserService;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }

  const mockUser: User = {
    id: 1,
    email: 'yoga@studio.com',
    lastName: 'Admin',
    firstName: 'Admin',
    password: 'password',
    admin: true,
    createdAt: new Date('2024-09-07 15:52:11'),
    updatedAt: new Date('2024-09-07 15:52:11')
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        UserService
      ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    userService = TestBed.inject(UserService);
    fixture.detectChanges();

    jest.spyOn(userService, 'getById').mockReturnValue(of(mockUser));

    component.ngOnInit();
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('integration', () => {
    it('should display user name', () => {
      const nameElement = fixture.debugElement.queryAll(By.css('p'))[0].nativeElement;
      expect(nameElement.textContent).toContain('Admin ADMIN');
    });
  });

  describe('integration', () => {
    it('should display user email', () => {
      const emailElement = fixture.debugElement.queryAll(By.css('p'))[1].nativeElement;
      expect(emailElement.textContent).toContain('yoga@studio.com');
    });
  });

  describe('integration', () => {
    it('should display admin status', () => {
      const adminStatusElement = fixture.debugElement.query(By.css('.my2')).nativeElement;
      expect(adminStatusElement.textContent).toContain('You are admin');
    });
  });

  it('should display createdAt date', () => {
    const createdAtElement = fixture.debugElement.query(By.css('.p2.w100 p')).nativeElement;
    expect(createdAtElement.textContent).toContain('September 7, 2024');
  });

  it('should display updatedAt date', () => {
    const updatedAtElement = fixture.debugElement.query(By.css('.p2.w100 p:last-of-type')).nativeElement;
    expect(updatedAtElement.textContent).toContain('September 7, 2024');
  });
});
