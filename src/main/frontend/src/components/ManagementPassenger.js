import {Component} from "react";
import {Button, Col, Modal, Row} from "react-bootstrap";
import {Message} from "semantic-ui-react";

class ManagementPassenger extends Component {

    constructor(props) {
        super(props);
        this.state = {
            error: false,
            messageError: '',
            firstname: '',
            lastname: '',
            email: '',
            phoneNumber: '',
            dataToEditing: false,
        }
    }

    componentDidMount() {
        if (this.props.passengerToEdit[0]) {
            this.setState({dataToEditing: true})
            this.loadDataForEditing(this.props.passengerToEdit[0])
        }
    }

    loadDataForEditing = (editedOffer) => {
        this.setState({
            firstname: editedOffer.firstname,
            lastname: editedOffer.lastname,
            email: editedOffer.email,
            phoneNumber: editedOffer.phoneNumber
        })
    }

    prepareData = () => {
        return {
            firstname: this.state.firstname,
            lastname: this.state.lastname,
            email: this.state.email,
            phoneNumber: this.state.phoneNumber
        }
    }

    saveThePassenger = async () => {
        const passengerRequest = this.prepareData()

        try {
            const response = await fetch("/api/passengers/addNewPassenger/"+this.props.flight, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(passengerRequest)
            });
            const responseData = await response.json();

            if (!response.ok) {
                throw new Error(responseData.message || 'Network response was not ok');
            }
            window.location.reload()
        } catch (error) {
            console.log(error);
            this.setState({
                error: true,
                messageError: error.message
            });
        }
    }

    editThePassenger = async () => {
        const passengerRequest = this.prepareData()

        try {
            const response = await fetch("/api/passengers/editPassenger/"+this.props.passengerToEdit[0].idPassenger, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(passengerRequest)
            });
            const responseData = await response.json();

            if (!response.ok) {
                throw new Error(responseData.message || 'Network response was not ok');
            }
            window.location.reload()
        } catch (error) {
            console.log(error);
            this.setState({
                error: true,
                messageError: error.message
            });
        }
    }

    render() {
        return (
            <Modal show={this.props.show} onHide={this.props.closeManagementPassenger}>
                <Modal.Header closeButton style={{ display: 'flex', justifyContent: 'center', alignItems: 'center'}}>
                    <Modal.Title style={{textAlign: 'center', margin: '0' }}>{this.props.mode}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <form style={{float: 'left'}}>
                        <Row className="mt-2">
                            <Col style={{textAlign: 'end'}}>
                                <p>Imię: </p>
                            </Col>
                            <Col>
                                <input type={"text"} value={this.state.firstname} placeholder={'John'} onChange={(e) => this.setState({firstname: e.target.value})}/>
                            </Col>
                        </Row>
                        <Row className="mt-2">
                            <Col style={{textAlign: 'end'}}>
                                <p>Nazwisko: </p>
                            </Col>
                            <Col>
                                <input type={"text"} value={this.state.lastname} placeholder={'Doe'} onChange={(e) => this.setState({lastname: e.target.value})}/>
                            </Col>
                        </Row>
                        <Row className="mt-2">
                            <Col style={{textAlign: 'end'}}>
                                <p>Email: </p>
                            </Col>
                            <Col>
                                <input type={"email"} value={this.state.email} placeholder={'john@example.com'} onChange={(e) => this.setState({email: e.target.value})}/>
                            </Col>
                        </Row>
                        <Row className="mt-2">
                            <Col style={{textAlign: 'end'}}>
                                <p>Numer telefonu: </p>
                            </Col>
                            <Col>
                                <input type={"tel"} value={this.state.phoneNumber} placeholder={'888999111'} onChange={(e) => this.setState({phoneNumber: e.target.value})}/>
                            </Col>
                        </Row>
                    </form>
                </Modal.Body>
                <Modal.Footer>
                    {this.state.error &&
                        <Message negative  className={"mt-3 messageError"} style={{width: '400px'}}>{this.state.messageError}</Message>
                    }
                    <Button variant="secondary" onClick={this.props.closeManagementPassenger}>
                        Zamknij
                    </Button>
                    {this.props.passengerToEdit[0] ? (
                        <Button onClick={this.editThePassenger}>
                            Edytuj lot
                        </Button>
                    ) : (
                        <Button onClick={this.saveThePassenger}>
                            Zapisz pasażera
                        </Button>
                    )
                    }
                </Modal.Footer>
            </Modal>
        )
    }

}
export default ManagementPassenger;