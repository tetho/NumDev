describe('Session specs', () => {
  beforeEach(() => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    });

    cy.intercept('GET', '/api/session', {
      body: [
        {
          id: 1,
          name: "Séance 1",
          date: "2024-09-07T00:00:00.000+00:00",
          teacher_id: 1,
          description: "Description de la session",
          users: [2],
        },
        {
          id: 2,
          name: "session 1",
          date: "2012-01-01T00:00:00.000+00:00",
          teacher_id: null,
          description: "my description",
          users: [],
        }
      ],
    }).as('findAllSessions');

    cy.intercept('GET', '/api/session/1', {
      body: {
        name: "Séance 1",
        date: "2024-09-07T00:00:00.000+00:00",
        teacher_id: 1,
        description: "Description de la session",
        users: [2],
        createdAt: "2024-09-07T15:59:10",
        updatedAt: "2024-09-07T15:59:10"
      }
    }).as('findSessionById');

    cy.intercept('GET', '/api/teacher', {
      body: [
        {
          id: 1,
          lastName: "DELAHAYE",
          firstName: "Margot",
        },
        {
          id: 2,
          lastName: "THIERCELIN",
          firstName: "Hélène",
        }
      ],
    }).as('findAllTeachers');

    cy.intercept('GET', '/api/teacher/1', {
      body: {
        id: 1,
        lastName: "DELAHAYE",
        firstName: "Margot",
      }
    }).as('findTeacherById');

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
  });

  it('should find all sessions', () => {
    cy.wait('@findAllSessions');

    cy.get('.list .item').should('have.length', 2);

    cy.get('.list .item').eq(0).within(() => {
      cy.get('.mat-card-title').should('contain', 'Séance 1');
      cy.get('.mat-card-subtitle').should('contain', 'Session on September 7, 2024');
      cy.get('mat-card-content p').should('contain', 'Description de la session');
    });

    cy.get('.list .item').eq(1).within(() => {
      cy.get('mat-card-title').should('contain', 'session 1');
      cy.get('mat-card-subtitle').should('contain', 'Session on January 1, 2012');
      cy.get('mat-card-content p').should('contain', 'my description');
    });
  });

  it('should find a session by id', () => {
    cy.get('.list .item').eq(0).within(() => {
      cy.get('button').contains('Detail').click();
    });

    cy.wait('@findSessionById');

    cy.url().should('include', '/sessions/detail/1');

    cy.get('mat-card-title h1').should('contain', 'Séance 1');
    cy.get('.ml3').contains('people').next('span').should('contain', 'Margot DELAHAYE');
    cy.get('.my2').contains('calendar_month').next('span').should('contain', 'September 7, 2024');
    cy.get('.my2').contains('group').next('span').should('contain', '1 attendees');
    cy.get('.description').contains('Description de la session');
    cy.get('.created').contains('September 7, 2024');
    cy.get('.updated').contains('September 7, 2024');
  });

  it('should create a session', () => {
    cy.get('button[routerLink="create"]').click();

    cy.url().should('include', '/sessions/create');

    cy.intercept('POST', '/api/session', {
      body: {
        name: 'name',
        date: 'date',
        teacher_id: 5,
        description: 'description'
      },
    })

    cy.get('input[formControlName=name]').type("session 1");
    cy.get('input[formControlName=date]').type("2012-01-01");
    cy.get('mat-select[formControlName=teacher_id]').click();
    cy.get('mat-option').contains('Margot DELAHAYE').click();
    cy.get('textarea[formControlName=description]').type("my description");
    cy.get('button[type="submit"]').click();

    cy.url().should('include', '/sessions')
  });

  it('should update a session', () => {
    cy.get('.list .item').eq(0).within(() => {
      cy.get('button').contains('Edit').click();
    });

    cy.wait('@findSessionById');

    cy.url().should('include', '/sessions/update/1');

    cy.intercept('PUT', '/api/session/1', {
      body: {
        name: 'name',
        date: 'date',
        teacher_id: 5,
        description: 'description'
      },
    })

    cy.get('input[formControlName=name]').type(" v2");
    cy.get('input[formControlName=date]').type("2012-01-01");
    cy.get('mat-select[formControlName=teacher_id]').click();
    cy.get('mat-option').contains('Hélène THIERCELIN').click();
    cy.get('textarea[formControlName=description]').type(" updated");
    cy.get('button[type="submit"]').click();

    cy.url().should('include', '/sessions')
  });

  it('should delete a session', () => {
    cy.get('.list .item').eq(0).within(() => {
      cy.get('button').contains('Detail').click();
    });

    cy.wait('@findSessionById');

    cy.url().should('include', '/sessions/detail/1');

    cy.intercept('DELETE', '/api/session/1', {
      body: {
        name: 'name',
        date: 'date',
        teacher_id: 'teacherId',
        description: 'description',
        createdAt: 'creationDate',
        updatedAt: 'updateDate'
      },
    })

    cy.get('button').contains('Delete').click();

    cy.url().should('include', '/sessions')
  });
});