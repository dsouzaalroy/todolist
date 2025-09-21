'use client'
import { useState } from "react";
import styles from "./page.module.css";
import { Task } from "@/components/Task";
import TaskLayout from "@/components/TaskLayout";

export default function Home() {
  const [title, setTitle] = useState("");
  const [tasks, setTasks] = useState<Task[]>([]);

  const createTask = async () => {
    const response = await fetch("http://localhost:8080/tasks/create", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        title: title
      }),
    })
    await listAllTasks();
  }

  const listAllTasks = async () => {
    const response = await (fetch("http://localhost:8080/tasks"));
    const data = await response.json();
    setTasks(data);
  }

  return (
    <div>
      <label>Title</label>
      <input value={title} onChange={e => setTitle(e.target.value)} />
      <button onClick={createTask}>Submit</button>
      <button onClick={listAllTasks}>List</button>
      {tasks.map(item => <TaskLayout key={item.id} task={item} />)}
    </div>
  );
}
