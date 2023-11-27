# ecommerce-ui

## Project setup
```
npm install
```

### Compiles and hot-reloads for development
```
npm run serve
```

### Compiles and minifies for production
```
npm run build
```

### Lints and fixes files
```
npm run lint
```

### Customize configuration
See [Configuration Reference](https://cli.vuejs.org/config/).


### Vue intro

#### 1) Code structure

```javascript
<template>
  <!-- HTML Template -->
</template>

<script>
  export default {}
</script>

<style>
</style>
```

The template part contains the HTML of the component.

The script tag contains the code defining the custom behavior of the component.

The style tag houses the CSS of the component.

src/components and src/views contain all our components.

#### 2) Folder Structure

Let’s go through the folder structure of our newly created Vue project

*public *— contains the main HTML file of our project

*src/assets *— stores the media files like images, logos, etc.

*src/components *— stores all the reusable components of our project. These components are not unique to some specific route.

Apart from this, we have some important files too

*App.vue *— it is the root component of our project

main.js **— it is the starting point of our project. Here we import our root component **App.vue, our router file index.js and createApp method. After this, we mount our root component to the DOM using the following statement:

new Vue({
render: h => h(App),
}).$mount('#app')

