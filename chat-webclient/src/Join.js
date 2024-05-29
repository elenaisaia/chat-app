import {useState} from "react";
import {useNavigate} from "react-router-dom";
import {Button, Card, CardBody, Col, Container, Form, Row} from "react-bootstrap";

export const Join = () => {
    const navigate = useNavigate()
    const [message, setMessage] = useState({
        sender: ""
    })

    const handleJoin = (e) => {
        e.preventDefault()
        console.log(message)
        if(message.sender === "") {
            alert("Name field must not be empty! :<")
        }
        else {
            navigate('/chat', {
                state: {
                    sender: message.sender
                }
            });
        }
    }

    const handleTextFieldChange = (name, e) => {
        e.preventDefault()
        const initialMessage = {...message}
        initialMessage[name] = e.target.value
        setMessage(initialMessage)
    }

    return (
        <Container fluid>
            <Row>
                <Col>
                    <h2> Hello! Join the chat here: </h2>
                </Col>
            </Row>
            <Row>
                <Col>
                    <Card>
                        <CardBody>
                            <Form onSubmit={handleJoin} id="joinForm">
                                <Row>
                                    <Col>
                                        <Form.Label> Please enter your name: </Form.Label>
                                        <Form.Control
                                            type="text"
                                            id="senderId"
                                            value={message.sender}
                                            onChange={(e) =>
                                                handleTextFieldChange("sender", e)}
                                        />
                                    </Col>
                                </Row>
                                <Row>
                                    <Col>
                                        <Button id="joinButtonId" type="submit">
                                            Join chat
                                        </Button>
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