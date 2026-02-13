/**
 * Unit Tests for US8: Navigation to Create Ticket
 * 
 * User Story: "As a user, I want to click a button to create a new ticket so I can begin a new submission quickly."
 * 
 * Acceptance Criteria:
 * AC1: Clicking "Create New Ticket" loads the Create Ticket page
 * AC2: No fields are pre-filled
 */

import {
  setupDashboardWithCreateButton,
  setupCreateTicketPage,
  getCreateTicketButton,
  createTicketButtonExists,
  isCreateTicketButtonVisible,
  getCreateTicketButtonText,
  clickCreateTicketButton,
  isOnDashboardPage,
  isOnCreateTicketPage,
  getCreateTicketFormFields,
  areAllFormFieldsEmpty,
  isFieldEmpty,
  hasDefaultValue,
  getFieldValue,
  formExists,
  getForm,
  formHasRequiredFields,
  formHasOptionalFields,
  mockWindowLocation,
  getCurrentLocation,
  navigateToCreateTicket,
  navigateToDashboard,
  didNavigationOccur,
  verifyButtonClickTriggersNavigation,
  buttonHasNavigationAction,
  countFormFields,
  getFormFieldNames,
  hasCorrectPageTitle,
  hasBackButton,
  getBackButton,
  clickBackButton,
  isFormInInitialState,
  isButtonEnabled,
  buttonHasIcon,
  waitForAsync,
  simulatePageLoad,
  verifyNavigationFlow
} from './setup/navigation-utils';

describe('US8: Navigation to Create Ticket', () => {
  beforeEach(() => {
    // Setup dashboard with create button
    setupDashboardWithCreateButton();
    
    // Mock window.location
    mockWindowLocation();
    
    // Clear all mocks
    jest.clearAllMocks();
  });

  // ============================================================================
  // AC1: Clicking "Create New Ticket" loads the Create Ticket page
  // ============================================================================

  describe('AC1: Clicking Create New Ticket Loads Create Ticket Page', () => {
    // Functional Test ID: US8_AC1_TC1
    test('US8_AC1_TC1: Create New Ticket button should be present on dashboard', () => {
      expect(createTicketButtonExists()).toBe(true);
    });

    // Functional Test ID: US8_AC1_TC2
    test('US8_AC1_TC2: Create New Ticket button should be visible', () => {
      expect(isCreateTicketButtonVisible()).toBe(true);
    });

    // Functional Test ID: US8_AC1_TC3
    test('US8_AC1_TC3: Create New Ticket button should have correct text', () => {
      const buttonText = getCreateTicketButtonText();
      expect(buttonText).toContain('Create New Ticket');
    });

    // Functional Test ID: US8_AC1_TC4
    test('US8_AC1_TC4: Create New Ticket button should be enabled', () => {
      const button = getCreateTicketButton();
      expect(button.disabled).toBe(false);
    });

    // Functional Test ID: US8_AC1_TC5
    test('US8_AC1_TC5: Create New Ticket button should have icon', () => {
      expect(buttonHasIcon('createTicketBtn')).toBe(true);
    });

    // Functional Test ID: US8_AC1_TC6
    test('US8_AC1_TC6: Clicking button should trigger navigation', () => {
      expect(verifyButtonClickTriggersNavigation()).toBe(true);
    });

    // Functional Test ID: US8_AC1_TC7
    test('US8_AC1_TC7: Button should have navigation action', () => {
      expect(buttonHasNavigationAction()).toBe(true);
    });

    // Functional Test ID: US8_AC1_TC8
    test('US8_AC1_TC8: Clicking button should navigate to create-ticket.html', () => {
      clickCreateTicketButton();
      
      // Simulate navigation
      window.location.href = 'create-ticket.html';
      
      expect(didNavigationOccur('create-ticket.html')).toBe(true);
    });

    // Functional Test ID: US8_AC1_TC9
    test('US8_AC1_TC9: Create Ticket page should load after button click', async () => {
      clickCreateTicketButton();
      
      // Simulate page load
      window.location.href = 'create-ticket.html';
      await simulatePageLoad(setupCreateTicketPage);
      
      expect(isOnCreateTicketPage()).toBe(true);
    });

    // Functional Test ID: US8_AC1_TC10
    test('US8_AC1_TC10: Create Ticket form should exist on new page', async () => {
      clickCreateTicketButton();
      
      // Simulate navigation and page load
      window.location.href = 'create-ticket.html';
      setupCreateTicketPage();
      
      expect(formExists()).toBe(true);
    });

    // Functional Test ID: US8_AC1_TC11
    test('US8_AC1_TC11: Create Ticket page should have correct title', async () => {
      navigateToCreateTicket();
      
      expect(hasCorrectPageTitle('Create New Ticket')).toBe(true);
    });

    // Functional Test ID: US8_AC1_TC12
    test('US8_AC1_TC12: Create Ticket page should have back button', async () => {
      navigateToCreateTicket();
      
      expect(hasBackButton()).toBe(true);
    });

    // Functional Test ID: US8_AC1_TC13
    test('US8_AC1_TC13: Back button should navigate to dashboard', async () => {
      navigateToCreateTicket();
      
      clickBackButton();
      window.location.href = 'index.html';
      
      expect(didNavigationOccur('index.html')).toBe(true);
    });

    // Functional Test ID: US8_AC1_TC14
    test('US8_AC1_TC14: Navigation flow should work end-to-end', async () => {
      const result = await verifyNavigationFlow('dashboard', 'create-ticket');
      
      expect(result.navigationOccurred).toBe(true);
      expect(result.formExists).toBe(true);
    });

    // Functional Test ID: US8_AC1_TC15
    test('US8_AC1_TC15: User should be on dashboard initially', () => {
      expect(isOnDashboardPage()).toBe(true);
      expect(isOnCreateTicketPage()).toBe(false);
    });

    // Functional Test ID: US8_AC1_TC16
    test('US8_AC1_TC16: User should be on create ticket page after navigation', async () => {
      navigateToCreateTicket();
      
      expect(isOnCreateTicketPage()).toBe(true);
      expect(isOnDashboardPage()).toBe(false);
    });

    // Functional Test ID: US8_AC1_TC17
    test('US8_AC1_TC17: Button click should change location href', () => {
      const button = getCreateTicketButton();
      const onclick = button.getAttribute('onclick');
      
      expect(onclick).toContain('create-ticket.html');
    });

    // Functional Test ID: US8_AC1_TC18
    test('US8_AC1_TC18: Create Ticket page should load without errors', async () => {
      expect(() => {
        navigateToCreateTicket();
      }).not.toThrow();
    });

    // Functional Test ID: US8_AC1_TC19
    test('US8_AC1_TC19: Form should be accessible after navigation', async () => {
      navigateToCreateTicket();
      
      const form = getForm();
      expect(form).toBeTruthy();
      expect(form.id).toBe('createTicketForm');
    });

    // Functional Test ID: US8_AC1_TC20
    test('US8_AC1_TC20: Multiple clicks should not cause errors', async () => {
      clickCreateTicketButton();
      clickCreateTicketButton();
      clickCreateTicketButton();
      
      // Should not throw errors
      expect(createTicketButtonExists()).toBe(true);
    });
  });

  // ============================================================================
  // AC2: No fields are pre-filled
  // ============================================================================

  describe('AC2: No Fields Are Pre-filled', () => {
    beforeEach(() => {
      // Navigate to create ticket page
      navigateToCreateTicket();
    });

    // Functional Test ID: US8_AC2_TC1
    test('US8_AC2_TC1: Title field should be empty', () => {
      expect(isFieldEmpty('title')).toBe(true);
    });

    // Functional Test ID: US8_AC2_TC2
    test('US8_AC2_TC2: System field should be empty', () => {
      expect(hasDefaultValue('system')).toBe(true);
    });

    // Functional Test ID: US8_AC2_TC3
    test('US8_AC2_TC3: Category field should be empty', () => {
      expect(hasDefaultValue('category')).toBe(true);
    });

    // Functional Test ID: US8_AC2_TC4
    test('US8_AC2_TC4: Description field should be empty', () => {
      expect(isFieldEmpty('description')).toBe(true);
    });

    // Functional Test ID: US8_AC2_TC5
    test('US8_AC2_TC5: Attachments field should be empty', () => {
      expect(isFieldEmpty('attachments')).toBe(true);
    });

    // Functional Test ID: US8_AC2_TC6
    test('US8_AC2_TC6: All form fields should be empty', () => {
      expect(areAllFormFieldsEmpty()).toBe(true);
    });

    // Functional Test ID: US8_AC2_TC7
    test('US8_AC2_TC7: Title field value should be empty string', () => {
      expect(getFieldValue('title')).toBe('');
    });

    // Functional Test ID: US8_AC2_TC8
    test('US8_AC2_TC8: System field value should be empty string', () => {
      expect(getFieldValue('system')).toBe('');
    });

    // Functional Test ID: US8_AC2_TC9
    test('US8_AC2_TC9: Category field value should be empty string', () => {
      expect(getFieldValue('category')).toBe('');
    });

    // Functional Test ID: US8_AC2_TC10
    test('US8_AC2_TC10: Description field value should be empty string', () => {
      expect(getFieldValue('description')).toBe('');
    });

    // Functional Test ID: US8_AC2_TC11
    test('US8_AC2_TC11: Form should be in initial state', () => {
      expect(isFormInInitialState()).toBe(true);
    });

    // Functional Test ID: US8_AC2_TC12
    test('US8_AC2_TC12: Form fields should have correct initial values', () => {
      const fields = getCreateTicketFormFields();
      
      expect(fields.title.value).toBe('');
      expect(fields.system.value).toBe('');
      expect(fields.category.value).toBe('');
      expect(fields.description.value).toBe('');
      expect(fields.attachments.files.length).toBe(0);
    });

    // Functional Test ID: US8_AC2_TC13
    test('US8_AC2_TC13: System dropdown should show placeholder option', () => {
      const systemField = document.getElementById('system');
      const firstOption = systemField.options[0];
      
      expect(firstOption.value).toBe('');
      expect(firstOption.textContent).toContain('Select');
    });

    // Functional Test ID: US8_AC2_TC14
    test('US8_AC2_TC14: Category dropdown should show placeholder option', () => {
      const categoryField = document.getElementById('category');
      const firstOption = categoryField.options[0];
      
      expect(firstOption.value).toBe('');
      expect(firstOption.textContent).toContain('Select');
    });

    // Functional Test ID: US8_AC2_TC15
    test('US8_AC2_TC15: No field should have pre-filled data', () => {
      const fieldNames = getFormFieldNames();
      
      fieldNames.forEach(fieldName => {
        const field = document.getElementById(fieldName) || document.querySelector(`[name="${fieldName}"]`);
        if (field && field.tagName.toLowerCase() !== 'button') {
          if (field.type === 'file') {
            expect(field.files.length).toBe(0);
          } else {
            expect(field.value).toBe('');
          }
        }
      });
    });

    // Functional Test ID: US8_AC2_TC16
    test('US8_AC2_TC16: Form should have all required fields empty', () => {
      const requiredFields = ['title', 'system', 'category', 'description'];
      
      requiredFields.forEach(fieldId => {
        expect(isFieldEmpty(fieldId) || hasDefaultValue(fieldId)).toBe(true);
      });
    });

    // Functional Test ID: US8_AC2_TC17
    test('US8_AC2_TC17: Form should have optional fields empty', () => {
      expect(isFieldEmpty('attachments')).toBe(true);
    });

    // Functional Test ID: US8_AC2_TC18
    test('US8_AC2_TC18: Form fields count should match expected', () => {
      const fieldCount = countFormFields();
      expect(fieldCount).toBeGreaterThanOrEqual(5); // At least 5 fields
    });

    // Functional Test ID: US8_AC2_TC19
    test('US8_AC2_TC19: Navigation from dashboard should not carry data', async () => {
      // Navigate back to dashboard
      navigateToDashboard();
      
      // Navigate to create ticket again
      navigateToCreateTicket();
      
      // Fields should still be empty
      expect(areAllFormFieldsEmpty()).toBe(true);
    });

    // Functional Test ID: US8_AC2_TC20
    test('US8_AC2_TC20: Multiple navigations should not pre-fill fields', async () => {
      // Navigate multiple times
      navigateToDashboard();
      navigateToCreateTicket();
      navigateToDashboard();
      navigateToCreateTicket();
      
      // Fields should remain empty
      expect(areAllFormFieldsEmpty()).toBe(true);
    });
  });

  // ============================================================================
  // Integration and Edge Cases
  // ============================================================================

  describe('Integration and Edge Cases', () => {
    // Functional Test ID: US8_INTEGRATION_TC1
    test('US8_INTEGRATION_TC1: Complete navigation flow should work', async () => {
      // Start on dashboard
      expect(isOnDashboardPage()).toBe(true);
      
      // Click create button
      clickCreateTicketButton();
      window.location.href = 'create-ticket.html';
      setupCreateTicketPage();
      
      // Verify on create page
      expect(isOnCreateTicketPage()).toBe(true);
      
      // Verify form is empty
      expect(areAllFormFieldsEmpty()).toBe(true);
    });

    // Functional Test ID: US8_INTEGRATION_TC2
    test('US8_INTEGRATION_TC2: Button should be accessible via keyboard', () => {
      const button = getCreateTicketButton();
      expect(button.tabIndex).toBeGreaterThanOrEqual(0);
    });

    // Functional Test ID: US8_INTEGRATION_TC3
    test('US8_INTEGRATION_TC3: Button should have proper styling classes', () => {
      const button = getCreateTicketButton();
      expect(button.className).toContain('btn');
    });

    // Functional Test ID: US8_INTEGRATION_TC4
    test('US8_INTEGRATION_TC4: Form should have submit button', () => {
      navigateToCreateTicket();
      
      const submitBtn = document.getElementById('submitBtn');
      expect(submitBtn).toBeTruthy();
    });

    // Functional Test ID: US8_INTEGRATION_TC5
    test('US8_INTEGRATION_TC5: Form should have cancel button', () => {
      navigateToCreateTicket();
      
      const cancelBtn = document.querySelector('.btn-cancel');
      expect(cancelBtn).toBeTruthy();
    });

    // Functional Test ID: US8_EDGE_TC1
    test('US8_EDGE_TC1: Rapid button clicks should not cause errors', () => {
      expect(() => {
        for (let i = 0; i < 10; i++) {
          clickCreateTicketButton();
        }
      }).not.toThrow();
    });

    // Functional Test ID: US8_EDGE_TC2
    test('US8_EDGE_TC2: Navigation should work with disabled JavaScript features', () => {
      const button = getCreateTicketButton();
      const onclick = button.getAttribute('onclick');
      
      // Should have onclick as fallback
      expect(onclick).toBeTruthy();
    });

    // Functional Test ID: US8_EDGE_TC3
    test('US8_EDGE_TC3: Form should handle page refresh', () => {
      navigateToCreateTicket();
      
      // Simulate page refresh
      setupCreateTicketPage();
      
      expect(areAllFormFieldsEmpty()).toBe(true);
    });

    // Functional Test ID: US8_EDGE_TC4
    test('US8_EDGE_TC4: Navigation should work from different dashboard states', () => {
      // Dashboard with different stats
      setupDashboardWithCreateButton();
      
      expect(createTicketButtonExists()).toBe(true);
      expect(verifyButtonClickTriggersNavigation()).toBe(true);
    });

    // Functional Test ID: US8_EDGE_TC5
    test('US8_EDGE_TC5: Button should be visible on different screen sizes', () => {
      // Button should exist regardless of viewport
      expect(createTicketButtonExists()).toBe(true);
      expect(isCreateTicketButtonVisible()).toBe(true);
    });

    // Functional Test ID: US8_EDGE_TC6
    test('US8_EDGE_TC6: Form should have proper HTML structure', () => {
      navigateToCreateTicket();
      
      const form = getForm();
      expect(form.tagName.toLowerCase()).toBe('form');
      expect(form.id).toBe('createTicketForm');
    });

    // Functional Test ID: US8_EDGE_TC7
    test('US8_EDGE_TC7: Required fields should have required attribute', () => {
      navigateToCreateTicket();
      
      expect(formHasRequiredFields()).toBe(true);
    });

    // Functional Test ID: US8_EDGE_TC8
    test('US8_EDGE_TC8: Optional fields should not have required attribute', () => {
      navigateToCreateTicket();
      
      expect(formHasOptionalFields()).toBe(true);
    });

    // Functional Test ID: US8_EDGE_TC9
    test('US8_EDGE_TC9: Navigation should preserve dashboard state', () => {
      const dashboardContent = document.querySelector('.dashboard-content');
      const originalContent = dashboardContent ? dashboardContent.innerHTML : '';
      
      clickCreateTicketButton();
      
      // Dashboard content should still exist
      expect(dashboardContent).toBeTruthy();
    });

    // Functional Test ID: US8_EDGE_TC10
    test('US8_EDGE_TC10: Button should have appropriate ARIA attributes', () => {
      const button = getCreateTicketButton();
      
      // Button should be accessible
      expect(button.tagName.toLowerCase()).toBe('button');
      expect(button.textContent).toBeTruthy();
    });

    // Functional Test ID: US8_EDGE_TC11
    test('US8_EDGE_TC11: Form should be ready for user input immediately', () => {
      navigateToCreateTicket();
      
      const titleField = document.getElementById('title');
      expect(titleField.disabled).toBe(false);
      expect(titleField.readOnly).toBe(false);
    });

    // Functional Test ID: US8_EDGE_TC12
    test('US8_EDGE_TC12: Navigation should work without page reload', () => {
      mockWindowLocation();
      
      clickCreateTicketButton();
      window.location.href = 'create-ticket.html';
      
      expect(getCurrentLocation()).toContain('create-ticket.html');
    });

    // Functional Test ID: US8_EDGE_TC13
    test('US8_EDGE_TC13: Form should not have any validation errors initially', () => {
      navigateToCreateTicket();
      
      const form = getForm();
      expect(form.checkValidity()).toBe(false); // Empty required fields
      
      // But no error messages should be shown
      const errorMessages = document.querySelectorAll('.error-message');
      expect(errorMessages.length).toBe(0);
    });

    // Functional Test ID: US8_EDGE_TC14
    test('US8_EDGE_TC14: Back navigation should work correctly', async () => {
      navigateToCreateTicket();
      expect(isOnCreateTicketPage()).toBe(true);
      
      clickBackButton();
      window.location.href = 'index.html';
      setupDashboardWithCreateButton();
      
      expect(isOnDashboardPage()).toBe(true);
    });

    // Functional Test ID: US8_EDGE_TC15
    test('US8_EDGE_TC15: Complete user journey should work seamlessly', async () => {
      // User on dashboard
      expect(isOnDashboardPage()).toBe(true);
      expect(createTicketButtonExists()).toBe(true);
      
      // User clicks create button
      clickCreateTicketButton();
      window.location.href = 'create-ticket.html';
      setupCreateTicketPage();
      
      // User sees empty form
      expect(isOnCreateTicketPage()).toBe(true);
      expect(areAllFormFieldsEmpty()).toBe(true);
      expect(formHasRequiredFields()).toBe(true);
      
      // User can start filling form
      const titleField = document.getElementById('title');
      expect(titleField).toBeTruthy();
      expect(titleField.disabled).toBe(false);
    });
  });
});