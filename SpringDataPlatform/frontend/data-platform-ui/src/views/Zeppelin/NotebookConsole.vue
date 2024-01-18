<template>
  <div>
    <h1>Notebook console</h1>
    <label>Select Notebook:</label>
    <select v-model="selectedNotebook">
      <option
        v-for="notebook in notebooks"
        :key="notebook.id"
        :value="notebook.zeppelinNoteId"
      >
        {{
          "Notebook ID = " +
          notebook.zeppelinNoteId +
          ",     Interpreter = " +
          notebook.interpreterGroup
        }}
      </option>
    </select>
    <button @click="addCell">Add Cell</button>
    <div v-for="(cell, index) in cells" :key="index">
      <textarea
        v-model="cell.code"
        placeholder="Type your code here"
      ></textarea>
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
import axios from "axios";
export default {
  data() {
    return {
      cells: [
        {
          code: "",
          result: undefined,
        },
      ],
      notebooks: [],
      selectedNotebook: null,
    };
  },
  methods: {
    async getNotebooks() {
      // fetch users
      await axios
        .get("http://localhost:9999/zeppelin/")
        .then((res) => {
          this.notebooks = res.data;
          console.log("this.notebooks = " + JSON.stringify(this.notebooks));
        })
        .catch((err) => console.log(err));
    },

    addCell() {
      this.cells.push({
        code: "",
        result: undefined,
      });
    },
    // executeCode(index) {
    //   // Mock code execution, replace with actual execution logic
    //   const result = this.mockExecuteCode(this.cells[index].code);
    //   console.log("Run on cell ID = " + this.selectedNotebook)
    //   this.$set(this.cells, index, {
    //     ...this.cells[index],
    //     result,
    //   });
    // },
    async executeCode(index) {
      console.log(
        "Run on cell ID = " + this.selectedNotebook + " index = " + index
      );
      const codeCmd = {
        noteId: this.selectedNotebook,
        text: "kkk ",
      };
      console.log("codeCmd = " + JSON.stringify(codeCmd));
      await axios({
        method: "post",
        url: "http://localhost:9999/zeppelin/addParagraph",
        data: JSON.stringify(codeCmd),
        headers: {
          "Content-Type": "application/json",
        },
      })
        .then((res) => {
          //sending the event to parent to handle
          console.log(res);
        })
        .catch((err) => console.log(err));
    },
    mockExecuteCode(code) {
      // Mock execution, replace with actual code execution logic
      return `Mock Result for Notebook ${this.selectedNotebook}: ${code}`;
    },
  },
  mounted() {
    this.getNotebooks();
  },
};
</script>

<style scoped>
textarea {
  width: 100%;
  height: 100px;
}
</style>
