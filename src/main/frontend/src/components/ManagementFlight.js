import {Component} from "react";
import {Button, Col, Modal, Row} from "react-bootstrap";
import {Message} from "semantic-ui-react";
import Datetime from 'react-datetime';
import 'react-datetime/css/react-datetime.css';
import moment from 'moment';
import TimePicker from 'react-time-picker';
import 'react-time-picker/dist/TimePicker.css';
import 'react-clock/dist/Clock.css';

class ManagementFlight extends Component {
    constructor(props) {
        super(props);
        this.state = {
        error: false,
        messageError: '',
        flightNumber: '',
        availableSeats: 0,
        departureCity: '',
        arrivalCity: '',
        departureDate: null,
        flightDuration: null,
        intermediateAirports: [{id: 0, airportName: ''}],
        dataToEditing: false,
    }}

    componentDidMount() {
        if (this.props.flightToEdit[0]) {
            this.setState({dataToEditing: true})
            this.loadDataForEditing(this.props.flightToEdit[0])
        }
    }

    loadDataForEditing = (editedOffer) => {
        const [hours, minutes] = editedOffer.flightDuration.split(':');
        const formattedFlightDuration = `${hours}:${minutes}`;

        this.setState({
            flightNumber: editedOffer.flightNumber,
            availableSeats: editedOffer.availableSeats,
            departureCity: editedOffer.flightRoute.departureCity,
            arrivalCity: editedOffer.flightRoute.arrivalCity,
            departureDate: moment(editedOffer.departureDate),
            flightDuration: formattedFlightDuration,
            intermediateAirports: editedOffer.flightRoute.intermediateAirports.map(airport => ({
                id: airport.idIntermediateAirport,
                airportName: airport.airportName
            }))
        })
    }

    prepareData = () => {
        const isoDate = this.state.departureDate ? this.state.departureDate.toISOString() : null;
        let airports = null;

        if (this.state.intermediateAirports && this.state.intermediateAirports.length > 0) {
            airports = this.state.intermediateAirports.map(airport => {
                return {
                    airportName: airport.airportName
                };
            });
        }
        console.log(this.state.flightDuration);
        return {
            flightNumber: this.state.flightNumber,
            availableSeats: this.state.availableSeats,
            departureDate: isoDate,
            flightDuration: `${this.state.flightDuration}:00`,
            departureCity: this.state.departureCity,
            arrivalCity: this.state.arrivalCity,
            intermediateAirports: airports
        }
    }

    editTheFlight = async () => {
        const flightRequest = this.prepareData()

        try {
            const response = await fetch("api/flights/editFlight/" + this.props.flightToEdit[0].idFlight, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(flightRequest)
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

    saveTheFlight = async () => {
       const flightRequest = this.prepareData()

        try {
            const response = await fetch("api/flights/addNewFlight", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(flightRequest)
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

    isFutureDate = current => {
        const now = moment();
        return moment(current).isAfter(now);
    };

    addIntermediateAirports = () => {
        this.setState(prevState => ({
            intermediateAirports: [...prevState.intermediateAirports, { id: prevState.nextAirportId, url: '' }],
            nextAirportId: prevState.nextAirportId + 1,
        }))
        this.setState({changesNotSaved: true})
    }

    removeIntermediateAirports = (id) => {
        this.setState(prevState => ({
            intermediateAirports: prevState.intermediateAirports.filter(name => name.id !== id)
        }));
        this.setState({changesNotSaved: true})
    }

    updateIntermediateAirports = (id, airportName) => {
        this.setState(prevState => ({
            intermediateAirports: prevState.intermediateAirports.map(name => name.id === id ? { ...name, airportName: airportName } : name)
        }));
        this.setState({changesNotSaved: true})
    }

    render() {
        return (
            <Modal show={this.props.show} onHide={this.props.closeManagementFlight}>
                <Modal.Header closeButton style={{ display: 'flex', justifyContent: 'center', alignItems: 'center'}}>
                    <Modal.Title style={{textAlign: 'center', margin: '0' }}>{this.props.mode}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <form style={{float: 'left'}}>
                        <Row className="mt-2">
                            <Col style={{textAlign: 'end'}}>
                                <p>Numer lotu: </p>
                            </Col>
                            <Col>
                                <input type={"text"} value={this.state.flightNumber} placeholder={'LO123'} onChange={(e) => this.setState({flightNumber: e.target.value})}
                                       disabled={!!this.props.flightToEdit && this.props.flightToEdit.length > 0} />
                            </Col>
                        </Row>
                        <Row className="mt-2">
                            <Col style={{textAlign: 'end'}}>
                                <p>Data wylotu: </p>
                            </Col>
                            <Col>
                                <Datetime
                                    value={this.state.departureDate}
                                    onChange={date => this.setState({departureDate: date})}
                                    dateFormat="YYYY-MM-DD"
                                    timeFormat="HH:mm"
                                    inputProps={{ placeholder: 'Wybierz datę i godzinę' }}
                                    isValidDate={this.isFutureDate}
                                />
                            </Col>
                        </Row>
                        <Row className="mt-2">
                            <Col style={{textAlign: 'end'}}>
                                <p>Czas trwania lotu: </p>
                            </Col>
                            <Col>
                                <TimePicker size={"lg"}
                                    onChange={time => this.setState({flightDuration: time})}
                                    value={this.state.flightDuration}
                                    format="HH:mm"
                                    disableClock={true}
                                />
                            </Col>
                        </Row>
                        <Row className="mt-2">
                            <Col style={{textAlign: 'end'}}>
                                <p>Ilość dostępnych miejsc: </p>
                            </Col>
                            <Col>
                                <input type={"number"} value={this.state.availableSeats} min={0} step={1} onChange={(e) => this.setState({availableSeats: e.target.value})}/>
                            </Col>
                        </Row>
                        <Row className="mt-2">
                            <Col style={{textAlign: 'end'}}>
                                <p>Miejsce startu: </p>
                            </Col>
                            <Col>
                                <input type={"text"} value={this.state.departureCity} onChange={(e) => this.setState({departureCity: e.target.value})}/>
                            </Col>
                        </Row>
                        <Row className="mt-2">
                            <Col style={{textAlign: 'end'}}>
                                <p>Miejsce docelowe: </p>
                            </Col>
                            <Col>
                                <input type={"text"} value={this.state.arrivalCity} onChange={(e) => this.setState({arrivalCity: e.target.value})}/>
                            </Col>
                        </Row>
                        {this.state.intermediateAirports.map((airportName, index) => (
                            <Row className="mt-2" key={airportName.id}>
                                <Col style={{textAlign: 'end'}}>
                                    <p>Przesiadka: </p>
                                </Col>
                                <Col style={{display:'flex'}}>
                                    <input type={"text"} value={airportName.airportName} onChange={(e) => this.updateIntermediateAirports(airportName.id, e.target.value)}/>
                                </Col>
                                <Col>
                                    {index === this.state.intermediateAirports.length - 1 && (
                                        <Button variant="outline-secondary" onClick={this.addIntermediateAirports}>+</Button>
                                    )}
                                    {this.state.intermediateAirports.length > 1 && (
                                        <Button variant="outline-secondary" onClick={() => this.removeIntermediateAirports(airportName.id)}>-</Button>
                                    )}
                                </Col>
                            </Row>
                        ))}
                    </form>
                </Modal.Body>
                <Modal.Footer>
                    {this.state.error &&
                        <Message negative  className={"mt-3 messageError"} style={{width: '400px'}}>{this.state.messageError}</Message>
                    }
                    <Button variant="secondary" onClick={this.props.closeManagementFlight}>
                        Zamknij
                    </Button>
                    {this.props.flightToEdit[0] ? (
                            <Button onClick={this.editTheFlight}>
                                Edytuj lot
                            </Button>
                        ) : (
                        <Button onClick={this.saveTheFlight}>
                    Zapisz lot
                </Button>
                        )
                    }
                </Modal.Footer>
            </Modal>
        )
    }
}

export default ManagementFlight;