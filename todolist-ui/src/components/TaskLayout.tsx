import { State, Task } from "./Task"
import "../styles/TaskLayout.css"
import { useState } from "react"

export default function TaskLayout({ task }: { task: Task }) {
  const [state, setState] = useState<State>(task.state)

  const handleUpdate = async (e: React.ChangeEvent<HTMLSelectElement>) => {
    console.log(task.state);
    const newState = e.target.value as State;
    const response = await fetch("http://localhost:8080/tasks/update", {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        id: task.id,
        state: newState
      }),
    })

    if (response.status != 200) {
      // TODO Placeholder until better error handling
      console.log(response);
      alert("Error occured when updating task!")
    }

    setState(newState);
    task.state = newState;
  }

  return (
    <div className="taskLayoutParent" key={task.id}>
      <a className="title">{task.title}</a>
      {/* <a className="desc">{task.description}</a> */}
      {/* <a className="desc">Temp description</a> */}
      {/* <h2>{task.state}</h2> */}
      <a className="dueDate">{task.dueDate == undefined ? "MISSING" : task.dueDate} </a>
      {/* <a className="state">{task.state}</a> */}
      <select className="state" onChange={handleUpdate} value={state.valueOf()}>
        {Object.values(State).map((value) => (
          <option key={value} value={value.valueOf()}>
            {value.valueOf()}
          </option>
        ))}
      </select>

    </div>
  );

}
