describe('Register spec', () => {
    it('should register successfully', () => {
        cy.visit('/register')

        cy.intercept('POST', '/api/auth/register', {
            body: {
                firstname: 'firstName',
                lastName: 'lastName',
                email: 'email',
                password: 'password'
            },
        })

        cy.get('input[formControlName=firstName]').type("firstname");
        cy.get('input[formControlName=lastName]').type("lastname");
        cy.get('input[formControlName=email]').type("user@email.com");
        cy.get('input[formControlName=password]').type(`${"test!1234"}`);
        cy.get('button[type="submit"]').click();

        cy.url().should('include', '/login');
    })
});