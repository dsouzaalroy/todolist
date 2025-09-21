import { Task } from "./Task"
import "../styles/TaskLayout.css"

export default function TaskLayout({ task }: { task: Task }) {

  return (
    <div className="taskLayoutParent" key={task.id}>
      <a className="title">{task.title}</a>
      {/* <a className="desc">{task.description}</a> */}
      {/* <a className="desc">Temp description</a> */}
      {/* <h2>{task.state}</h2> */}
      <a className="dueDate">{task.dueDate == undefined ? "MISSING" : task.dueDate} </a>
      <a className="state">{task.state}</a>
    </div>
  );

}
