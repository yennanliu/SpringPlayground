<template>
    <div>
        <h1>Notebook console</h1>
      <button @click="addCell">Add Cell</button>
      <div v-for="(cell, index) in cells" :key="index">
        <textarea v-model="cell.code" placeholder="Type your code here"></textarea>
        <button @click="executeCode(index)">Run Code</button>
        <div v-if="cell.result !== undefined">
          <strong>Result:</strong>
          <pre>{{ cell.result }}</pre>
        </div>
        <hr />
      </div>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        cells: [
          {
            code: '',
            result: undefined,
          },
        ],
      };
    },
    methods: {
      addCell() {
        this.cells.push({
          code: '',
          result: undefined,
        });
      },
      executeCode(index) {
        // Mock code execution, replace with actual execution logic
        const result = this.mockExecuteCode(this.cells[index].code);
        this.$set(this.cells, index, {
          ...this.cells[index],
          result,
        });
      },
      mockExecuteCode(code) {
        // Mock execution, replace with actual code execution logic
        return `Mock Result: ${code}`;
      },
    },
  };
  </script>
  
  <style scoped>
  textarea {
    width: 100%;
    height: 100px;
  }
  </style>
  