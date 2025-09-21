export interface Task {
  id: number;
  title: string;
  description?: string;
  dueDate?: string;
  state: State;
}

export type TaskDTO = Omit<Task, 'id' | 'state'>

export enum State {
  READY = "READY",
  DOING = "DOING",
  COMPLETED = "COMPLETED"
}
