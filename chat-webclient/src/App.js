import './App.css';
import {BrowserRouter, Routes, Route} from "react-router-dom";
import {Chat} from "./Chat";
import {Join} from "./Join";

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Join/>}/>
                <Route path="/chat" element={<Chat serverUrl={"http://localhost:9090"}/>}/>
            </Routes>
        </BrowserRouter>
    );
}

export default App;
