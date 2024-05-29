import {useEffect, useRef, useState} from "react";
import axios from "axios";
import {useLocation, useNavigate} from "react-router-dom";
import {Button, Card, CardBody, Col, Container, Form, Row} from "react-bootstrap";
import {ChatMessage} from "./ChatMessage";

export const Chat = ({serverUrl}) => {
    const location = useLocation();
    const navigate = useNavigate();

    const [message, setMessage] = useState({
        sender: location.state.sender,
        text: "",
        session: ""
    });
    const [receivedMessage, setReceivedMessage] = useState(null);
    const [messageList, setMessageList] = useState([]);
    const ws = useRef(null);

    const handleSend = (e) => {
        e.preventDefault()
        console.log(message)
        if(message.text === "") {
            alert("Message must not be empty! :<")
        }
        else {
            axios.post("http://localhost:8080/messages", message)
                .catch((err) => {
                    if(err.response.status === 400) {
                        alert("Sender and text must not be null! :<")
                    }
                    else {
                        alert("Internal server error! Message not sent! Please try again! :<")
                    }
                })
                .then((res) => {
                    if(res) {
                        if(res.status === 200) {
                            console.log(message.text + " sent")
                        }
                    }
                })
            setMessage({...message, text: ""})
        }
    }

    const handleTextFieldChange = (name, e) => {
        e.preventDefault()
        const initialMessage = {...message}
        initialMessage[name] = e.target.value
        setMessage(initialMessage)
    }

    const handleLeave = (e) => {
        e.preventDefault()
        console.log(message.sender + " leaving")
        if (ws.current) {
            ws.current.close();
        }
        navigate('/', {
            state: {
                sender: message.sender
            }
        });
    }

    useEffect(() => {
        if (receivedMessage) {
            const messagesCopy = messageList.splice(0);
            messagesCopy.push(receivedMessage);
            setMessageList(messagesCopy);
            setReceivedMessage(null);
        }
    }, [receivedMessage, messageList])

    useEffect(() => {
        if (!ws.current) {
            ws.current = new WebSocket(serverUrl);
            ws.current.onopen = () => {
                console.log("Connection was opened. Sending join message");
                const joinMessage = {
                    type: "JOIN",
                    sender: message.sender
                }
                ws.current.send(JSON.stringify(joinMessage));
            };
            ws.current.onclose = () => {
                console.log("Connection was closed.")
            };
            ws.current.onmessage = (event) => {
                const msg = JSON.parse(event.data);
                if (msg) {
                    console.log("Received message: ");
                    console.log(msg);
                    if (msg.type && msg.type === "CURRENT_SESSION") {
                        setMessage({...message, session: msg.session})
                    }
                    else {
                        setReceivedMessage(msg);
                    }
                }
            }
        }
    })

    return (
        <Container fluid>
            <Row>
                <Col>
                    <h2> Welcome to the chat! :> </h2>
                </Col>
            </Row>
            <Row>
                <Col>
                    <Card>
                        <CardBody>
                            <Form onSubmit={handleLeave} id="leaveForm">
                                <Row>
                                    <Col>
                                        <Button id="leaveButtonId" type="submit">
                                            Leave chat
                                        </Button>
                                    </Col>
                                </Row>
                            </Form>
                            <div>
                                {
                                    messageList.map(msg =>
                                        <ChatMessage message={msg} key={msg.id}/>
                                    )
                                }
                            </div>
                            <Form onSubmit={handleSend} id="chatForm">
                                <Row>
                                    <Col>
                                        <Button id="sendButtonId" type="submit">
                                            Send
                                        </Button>
                                        <Form.Control
                                            type="text"
                                            id="textId"
                                            value={message.text}
                                            onChange={(e) =>
                                                handleTextFieldChange("text", e)}
                                        />
                                    </Col>
                                </Row>
                            </Form>
                        </CardBody>
                    </Card>
                </Col>
            </Row>
        </Container>
    )
}