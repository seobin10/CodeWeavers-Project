import "./App.css";
import { Provider } from "react-redux";
import store from "./store";
import AppWrapper from "./components/AppWrapper";

function App() {
  return (
    <Provider store={store}>
      <AppWrapper />
    </Provider>
  );
}

export default App;
