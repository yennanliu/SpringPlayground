module.exports = {
  preset: '@vue/cli-plugin-unit-jest',
  testMatch: [
    '**/tests/unit/**/*.spec.[jt]s?(x)',
    '**/__tests__/*.[jt]s?(x)'
  ],
  moduleFileExtensions: ['js', 'ts', 'json', 'vue'],
  moduleNameMapper: {
    '^@/(.*)$': '<rootDir>/src/$1'
  },
  transform: {
    '^.+\\.vue$': '@vue/vue3-jest',
    '^.+\\.[jt]sx?$': 'babel-jest'
  },
  transformIgnorePatterns: [
    '/node_modules/(?!axios)/'
  ],
  collectCoverage: true,
  collectCoverageFrom: [
    'src/**/*.{js,ts,vue}',
    '!src/main.ts',
    '!**/node_modules/**'
  ],
  coverageThreshold: {
    global: {
      branches: 0,
      functions: 0,
      lines: 0,
      statements: 0
    }
  },
  setupFilesAfterEnv: ['<rootDir>/tests/setup.js']
}
