import Vue from 'vue'
import { ValidationProvider, ValidationObserver, setInteractionMode } from 'vee-validate'

// Import and register rules
import './rules'

// Set interaction mode - validates on blur and on input after first blur
setInteractionMode('eager')

// Register components globally
Vue.component('ValidationProvider', ValidationProvider)
Vue.component('ValidationObserver', ValidationObserver)

export { ValidationProvider, ValidationObserver }
export * from './rules'
