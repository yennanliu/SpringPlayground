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
      <button @click="addAndRunCell(index)">Run Code</button>

      <!-- Result Cell -->
      <div v-if="cell.executionResult !== undefined">
        <strong>Execution Result:</strong>
        <pre>{{ cell.executionResult }}</pre>
      </div>
      <!-- <div>
        <strong>Execution Result:</strong>
        <pre>{{ cell.executionResult }}</pre>
      </div> -->
      <hr />

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
      console.log("new cell added !");
      this.cells.push({
        code: "",
        result: undefined,
        executionResult: undefined, // Added result field for execution result
      });
    },
    async addAndRunCell(index) {
      console.log(
        "Run on cell ID = " + this.selectedNotebook + " index = " + index
      );

      const addCellCmd = {
        noteId: this.selectedNotebook,
        text: this.cells[index].code,
      };

      console.log("codeCmd = " + JSON.stringify(addCellCmd));

      // Step 1) send "add paragraph" request to backend
      await axios({
        method: "post",
        url: "http://localhost:9999/zeppelin/addParagraph",
        data: JSON.stringify(addCellCmd),
        headers: {
          "Content-Type": "application/json",
        },
      })
        .then((res) => {
          // console.log(res);
          // // Handle the result as needed
          // this.$set(this.cells, index, {
          //   ...this.cells[index],
          //   result: res.data.result, // Assuming the result is available in the response
          // });
          console.log("(addCell) res = " + JSON.stringify(res));
          // Handle the result as needed

          this.runCell();

          this.$set(this.cells, index, {
            ...this.cells[index],
            result: res.data.result, // Assuming the result is available in the response
            executionResult: "dummy result", //res.data.executionResult, // Assuming execution result is available in the response
          });
        })
        .catch((err) => console.log(err));
    },
    async runCell() {
      console.log("run cell !");
      const runCmd = {
        noteId: this.selectedNotebook,
        paragraphId: this.data, //"paragraph_1705661212924_1271688536", //1// this.cells[index].code,
      };

      console.log(">>> runCell = " + JSON.stringify(runCmd));
      await axios({
        method: "post",
        url: "http://localhost:9999/zeppelin/execute_paragraph",
        data: JSON.stringify(runCmd),
        headers: {
          "Content-Type": "application/json",
        },
      }).then((res) => {
        console.log("(runCell) res = " + JSON.stringify(res));
      });
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
