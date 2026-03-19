// User types
export interface User {
  id?: number
  email: string
  firstName?: string
  lastName?: string
  password?: string
}

export interface SigninCredentials {
  email: string
  password: string
}

export interface SignupData extends SigninCredentials {
  firstName: string
  lastName: string
}

export interface AuthResponse {
  token: string
  user?: User
}

// Jar types
export interface Jar {
  id: number
  name: string
  filePath?: string
  uploadedAt?: string
  createdAt?: string
}

// Job types
export interface Job {
  id: number
  jobId?: string
  name?: string
  state?: string
  jarId?: number
  parallelism?: number
  entryClass?: string | null
  programArgs?: string | null
  savePointPath?: string | null
  allowNonRestoredState?: boolean | null
  createdAt?: string
  startTime?: string
  endTime?: string
}

export interface JobCreateData {
  jarId: number | string
  parallelism?: number
  entryClass?: string
  programArgs?: string
  savePointPath?: string
  allowNonRestoredState?: boolean
}

export interface SqlJobCreateData {
  statement: string
}

// Cluster types
export interface Cluster {
  id: number
  url: string
  port: number | string
  status?: string
  createdAt?: string
}

export interface ClusterCreateData {
  url: string
  port: number | string
}

// Notebook types
export interface Notebook {
  id: string
  name: string
  path?: string
  interpreterGroup?: string
  createdAt?: string
}

export interface NotebookCreateData {
  notePath: string
  interpreterGroup: string
}

// API response types
export interface ApiResponse<T> {
  data: T
  message?: string
  status?: number
}

// Table column type for DataTable component
export interface TableColumn {
  key: string
  label: string
  formatter?: (value: unknown) => string
}

// Select option type for FormField component
export interface SelectOption {
  value: string | number
  label: string
}
