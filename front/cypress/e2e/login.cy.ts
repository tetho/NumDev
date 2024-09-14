describe('Login spec', () => {
  it('should login successfully', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
  });

  it('should show user information', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')

    cy.intercept('GET', '/api/user/1', {
      body: {
        id: 1,
        email: "yoga@studio.com",
        lastName: "TEST",
        firstName: "Utilisateur",
        admin: true,
        createdAt: "2024-09-07T16:01:29",
        updatedAt: "2024-09-07T16:01:29"
      }
    }).as('findSessionById');

    cy.get('span[routerLink="me"]').should('exist').and('be.visible').click();

    cy.url().should('include', '/me');

    cy.get('mat-card-title h1').should('contain', 'User information');
    cy.get('mat-card-content').within(() => {
      cy.contains('p', 'Name: Utilisateur TEST');
      cy.contains('p', 'Email: yoga@studio.com');
    });
    cy.get('mat-card-content').within(() => {
      cy.contains('p', 'Create at: September 7, 2024');
      cy.contains('p', 'Last update: September 7, 2024');
    });
  });
});