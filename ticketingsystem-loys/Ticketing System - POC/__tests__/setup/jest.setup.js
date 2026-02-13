/**
 * Jest Setup File
 * Global configuration and utilities for all tests
 */

// Extend Jest matchers if needed
expect.extend({
  toBeVisible(element) {
    const pass = element && element.style.display !== 'none' && element.style.visibility !== 'hidden';
    return {
      pass,
      message: () => pass
        ? `Expected element not to be visible`
        : `Expected element to be visible`
    };
  }
});

// Global test utilities
global.createMockElement = (id, tag = 'div') => {
  const element = document.createElement(tag);
  element.id = id;
  return element;
};

// Mock console methods to reduce noise in tests
global.console = {
  ...console,
  error: jest.fn(),
  warn: jest.fn(),
  log: jest.fn()
};

// Setup DOM before each test
beforeEach(() => {
  // Clear document body
  document.body.innerHTML = '';
  
  // Reset all mocks
  jest.clearAllMocks();
});

// Cleanup after each test
afterEach(() => {
  // Clear any timers
  jest.clearAllTimers();
});