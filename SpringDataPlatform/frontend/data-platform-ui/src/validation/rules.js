import { extend } from 'vee-validate'
import { required, email, min, confirmed } from 'vee-validate/dist/rules'
import * as yup from 'yup'

// Register built-in rules
extend('required', {
  ...required,
  message: '{_field_} is required'
})

extend('email', {
  ...email,
  message: 'Please enter a valid email address'
})

extend('min', {
  ...min,
  message: '{_field_} must be at least {length} characters'
})

extend('confirmed', {
  ...confirmed,
  message: 'Passwords do not match'
})

// Custom rules
extend('url', {
  validate: (value) => {
    if (!value) return true
    try {
      new URL(value.startsWith('http') ? value : `http://${value}`)
      return true
    } catch {
      return false
    }
  },
  message: 'Please enter a valid URL'
})

extend('port', {
  validate: (value) => {
    if (!value) return true
    const port = parseInt(value, 10)
    return port >= 1 && port <= 65535
  },
  message: 'Please enter a valid port number (1-65535)'
})

extend('jarFile', {
  validate: (value) => {
    if (!value) return false
    if (value instanceof File) {
      return value.name.endsWith('.jar')
    }
    return String(value).endsWith('.jar')
  },
  message: 'Please select a valid JAR file'
})

// Yup schemas for complex validation
export const signinSchema = yup.object({
  email: yup.string().required('Email is required').email('Invalid email format'),
  password: yup.string().required('Password is required').min(6, 'Password must be at least 6 characters')
})

export const signupSchema = yup.object({
  email: yup.string().required('Email is required').email('Invalid email format'),
  firstName: yup.string().required('First name is required'),
  lastName: yup.string().required('Last name is required'),
  password: yup.string().required('Password is required').min(6, 'Password must be at least 6 characters'),
  passwordConfirm: yup.string().required('Please confirm your password').oneOf([yup.ref('password')], 'Passwords must match')
})

export const clusterSchema = yup.object({
  url: yup.string().required('URL is required'),
  port: yup.number().required('Port is required').min(1).max(65535)
})

export const notebookSchema = yup.object({
  notePath: yup.string().required('Notebook name is required'),
  interpreterGroup: yup.string().required('Interpreter is required')
})

export const sqlJobSchema = yup.object({
  statement: yup.string().required('SQL statement is required')
})
