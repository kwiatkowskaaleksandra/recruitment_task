import {Component} from "react";
import "./Flights.css"
import Table from 'react-bootstrap/Table';
import {Button, Col, Modal, Row} from "react-bootstrap";
import { AiFillEdit } from "react-icons/ai";
import { HiTrash } from "react-icons/hi2";
import ManagementFlight from "./ManagementFlight";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";


class Flights extends Component {

    state = {
        flights: [],
        showManagementFlightModal: false,
        mode: '',
        page: 1,
        size: 8,
        howManyPages: 1,
        selectedFlightId: 0,
        showDeleteModal: false,
        flightNumber: '',
        departureCity: '',
        arrivalCity: '',
        departureDate: null,
        availableSeats: 0,
    }

    async componentDidMount() {
        await fetch('/api/flights/countFlights', {
            method: 'GET',
        }).then(async response => {
            const data = await response.json();
            const howMany = Math.ceil(data / this.state.size);
            this.setState({howManyPages: howMany});
            this.getAllFlights().catch(err => {
                console.log(err)
            })
        })
    }

    getAllFlights = async (page) => {
        const size = this.state.size
        let currentPage = page !== undefined ? page : 1;

        if (page < 1) currentPage = 1
        else if (page > this.state.howManyPages) currentPage = this.state.howManyPages

        const url = new URL('http://localhost:8080/api/flights/getAll');
        url.searchParams.append('page', currentPage - 1);
        url.searchParams.append('size', size);

        await fetch(url, {
            method: 'GET',
        }).then(async response => {
            const data = await response.json();
            this.setState({flights: data.content})
        })
    }

    handleChangePage = (currentPage) => {
        this.setState({page: currentPage})
        this.getAllFlights(currentPage).catch(err => console.log(err))
    }

    formatDate(dateString) {
        const date = new Date(dateString);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
    }

    formatDuration(durationString) {
        const [hours, minutes] = durationString.split(':');
        let formattedDuration = '';

        if (parseInt(hours) > 0) {
            formattedDuration += parseInt(hours) + 'h ';
        }

        if (parseInt(minutes) > 0) {
            formattedDuration += parseInt(minutes) + 'min';
        }

        return formattedDuration;
    }

    deleteTheFlight = async () => {
        await fetch("/api/flights/deleteTheFlight/" + this.state.selectedFlightId, {
            method: 'DELETE',
        }).then(() => {
            window.location.reload()
        }).catch (error => {
            console.log(error);
        })
    }

    filteringFlights = async () => {
        const isoDate = this.state.departureDate
            ? new Date(this.state.departureDate.getTime() - (this.state.departureDate.getTimezoneOffset() * 60000)).toISOString().slice(0, 10)
            : null;


        const url = new URL('http://localhost:8080/api/flights/searchFlights');
        url.searchParams.append('flightNumber', this.state.flightNumber);
        url.searchParams.append('departureCity', this.state.departureCity);
        url.searchParams.append('arrivalCity', this.state.arrivalCity);
        url.searchParams.append('departureDate', isoDate);
        url.searchParams.append('availableSeats', this.state.availableSeats);

        await fetch(url, {
            method: 'GET'
        }).then(async response => {
            const data = await response.json();
            console.log(data)
            this.setState({flights: data})
        })
    }

    render() {
        const pages = Array.from({ length: this.state.howManyPages }, (_, index) => index + 1);
        return (
            <div className={"header"}>
                <p className={"flight-header"}>Lista lotów</p>
                <div>
                    <form style={{float: 'left'}}>
                        <Row className="mt-2">
                            <Col style={{textAlign: 'end'}}>
                                <p>Numer lotu: </p>
                            </Col>
                            <Col>
                                <input type={"text"} value={this.state.flightNumber} placeholder={'numer lotu'} onChange={(e) => this.setState({flightNumber: e.target.value})}/>
                            </Col>
                        </Row>
                        <Row className="mt-2">
                            <Col style={{textAlign: 'end'}}>
                                <p>Data wylotu: </p>
                            </Col>
                            <Col>
                                <DatePicker
                                    selected={this.state.departureDate}
                                    onChange={date => this.setState({departureDate: date})}
                                    dateFormat="yyyy-MM-dd"
                                    placeholderText='Wybierz datę'
                                    showYearDropdown
                                    scrollableMonthYearDropdown
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
                        <Row className="mt-2">
                            <Col style={{textAlign: 'end'}}>
                                <Button style={{marginBottom: '2%'}} onClick={this.filteringFlights}>Wyszukaj</Button>
                            </Col>
                        </Row>
                    </form>
                </div>
                <div style={{marginBottom: '2%', float: 'inline-end'}}>
                    <Button onClick={() => this.setState({showManagementFlightModal: true, mode: 'Dodaj nowy lot'})}>Dodaj lot</Button>
                </div>
                <Table striped bordered hover variant="light">
                    <thead>
                    <tr style={{textAlign: 'center'}}>
                        <th>ID</th>
                        <th>Numer lotu</th>
                        <th>Data wylotu</th>
                        <th>Godzina wylotu</th>
                        <th>Miejsce startu</th>
                        <th>Miejsce docelowe</th>
                        <th>Przesiadki</th>
                        <th>Ilość dostępnych miejsc</th>
                        <th>Czas trwania lotu</th>
                        <th>Akcje</th>
                    </tr>
                    </thead>
                    <tbody>
                    {this.state.flights.map((flight, index) => (
                        <tr key={index}>
                            <td>{flight.idFlight}</td>
                            <td>{flight.flightNumber}</td>
                            <td>{this.formatDate(flight.departureDate)}</td>
                            <td>{new Date(flight.departureDate).toLocaleTimeString()}</td>
                            <td>{flight.flightRoute.departureCity}</td>
                            <td>{flight.flightRoute.arrivalCity}</td>
                            <td>{flight.flightRoute.intermediateAirports
                                .sort((a, b) => {
                                    return (a.idIntermediateAirport > b.idIntermediateAirport) ? 1 : -1
                                })
                                .map((airport, index) => (
                                    <span key={index}>{airport.airportName} <br/></span>
                                ))}

                            </td>
                            <td>{flight.availableSeats}</td>
                            <td>{this.formatDuration(flight.flightDuration)}</td>
                            <td className="buttonContainer" style={{textAlign: 'center'}}>
                                <Button onClick={() => window.location.href = '/passengers/'+flight.flightNumber+'/'+flight.idFlight}>Lista pasażerów</Button><br/>
                                <Button style={{marginRight: '2%'}} onClick={() => this.setState({selectedFlightId: flight.idFlight, showManagementFlightModal: true, mode: 'Edytuj lot o numerze: ' + flight.flightNumber})}><AiFillEdit/></Button>
                                <Button onClick={() => {this.setState({showDeleteModal: true,  selectedFlightId: flight.idFlight})}}><HiTrash/></Button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </Table>
                <section className={"mt-5"}>
                    <nav aria-label="Page navigation example">
                        <ul className="pagination justify-content-end">
                            <li className="page-item">
                                <button className="page-link" onClick={() => this.handleChangePage(this.state.page - 1)} aria-label="Previous">
                                    <span aria-hidden="true">&laquo;</span>
                                </button>
                            </li>
                            {pages.map(pageNumber => (
                                <li className={`page-item ${this.state.page === pageNumber ? 'active' : ''}`} key={pageNumber}>
                                    <button className="page-link" onClick={() => this.handleChangePage(pageNumber)}>
                                        {pageNumber}
                                    </button>
                                </li>
                            ))}
                            <li className="page-item">
                                <button className="page-link" onClick={() => this.handleChangePage(this.state.page + 1)} aria-label="Next">
                                    <span aria-hidden="true">&raquo;</span>
                                </button>
                            </li>
                        </ul>
                    </nav>
                </section>


                {this.state.showManagementFlightModal && (
                    <div className="modal-container">
                        <ManagementFlight show={this.state.showManagementFlightModal} closeManagementFlight={() => this.setState({ showManagementFlightModal: false, selectedFlightId: 0})} mode={this.state.mode}
                                          flightToEdit={this.state.flights.filter(flight => flight.idFlight === this.state.selectedFlightId)}/>
                    </div>
                )}

                {this.state.showDeleteModal && (
                    <Modal show={this.state.showDeleteModal} onHide={() => this.setState({showDeleteModal: false})} size={"lg"}>
                        <Modal.Header closeButton>
                            <Modal.Title id="example-modal-sizes-title-xl">
                                {this.state.flights
                                    .filter(flight => flight.idFlight === this.state.selectedFlightId)
                                    .map(flight => 'Potwierdzenie usunięcia lotu o numerze: ' + flight.flightNumber)}
                            </Modal.Title>
                        </Modal.Header>
                        <Modal.Footer>
                            <Button variant="secondary" onClick={() => this.setState({ showDeleteModal: false })}>Zamknij</Button>
                            <Button variant="primary" onClick={this.deleteTheFlight}>Potwierdź</Button>
                        </Modal.Footer>
                    </Modal>
                )}
            </div>
        )
    }
}
export default Flights