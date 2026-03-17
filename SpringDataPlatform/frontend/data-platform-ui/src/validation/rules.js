import { defineRule, configure } from 'vee-validate'
import { required, email, min, confirmed } from '@vee-validate/rules'
import * as yup from 'yup'

// Register built-in rules
defineRule('required', required)
defineRule('email', email)
defineRule('min', min)
defineRule('confirmed', confirmed)

// Custom rules
defineRule('url', (value) => {
  if (!value) return true
  try {
    new URL(value.startsWith('http') ? value : `http://${value}`)
    return true
  } catch {
    return 'Please enter a valid URL'
  }
})

defineRule('port', (value) => {
  if (!value) return true
  const port = parseInt(value, 10)
  if (port >= 1 && port <= 65535) return true
  return 'Please enter a valid port number (1-65535)'
})

defineRule('jarFile', (value) => {
  if (!value) return 'Please select a JAR file'
  if (value instanceof File) {
    if (value.name.endsWith('.jar')) return true
  }
  if (String(value).endsWith('.jar')) return true
  return 'Please select a valid JAR file'
})

// Configure default messages
configure({
  generateMessage: (context) => {
    const messages = {
      required: `${context.field} is required`,
      email: 'Please enter a valid email address',
      min: `${context.field} must be at least ${context.rule.params[0]} characters`,
      confirmed: 'Passwords do not match'
    }
    return messages[context.rule.name] || `${context.field} is invalid`
  }
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
