import {Component} from "react";
import {Button, Modal} from "react-bootstrap";
import Table from "react-bootstrap/Table";
import {HiTrash} from "react-icons/hi2";
import {AiFillEdit} from "react-icons/ai";
import Flights from "./Flights";
import ManagementPassenger from "./ManagementPassenger";

class Passengers extends Component {

    state = {
        passengers: [],
        showManagementPassengersModal: false,
        mode: '',
        page: 1,
        size: 8,
        howManyPages: 1,
        selectedPassengerId: 0,
        showDeletePassengerModal: false,
        idFlight: 0,
        flightNumber: ''
    }

    async componentDidMount() {
        const url = window.location.pathname;
        const id = url.substring(url.lastIndexOf('/') + 1);
        const parts = url.split('/');
        const flightNumber = parts[parts.length - 2];
        this.setState({idFlight: id, flightNumber: flightNumber})
        await fetch("/api/passengers/countPassengersByIdFlight/"+id, {
            method: 'GET'
        }).then(async response => {
            const data = await response.json()
            const howMany = Math.ceil(data / this.state.size);
            this.setState({howManyPages: howMany});
            this.getAllPassengersByIdFlight().catch(err => console.log(err))
        })
    }

    getAllPassengersByIdFlight = async (page) => {
        const size = this.state.size
        let currentPage = page !== undefined ? page : 1;

        if (page < 1) currentPage = 1
        else if (page > this.state.howManyPages) currentPage = this.state.howManyPages

        const url = new URL('http://localhost:8080/api/passengers/getAllByIdFlight');
        url.searchParams.append('page', currentPage - 1);
        url.searchParams.append('size', size);
        url.searchParams.append('idFlight', this.state.idFlight)

        await fetch(url, {
            method: 'GET',
        }).then(async response => {
            const data = await response.json();
            this.setState({passengers: data.content})
        })
    }

    handleChangePage = (currentPage) => {
        this.setState({page: currentPage})
        this.getAllPassengersByIdFlight(currentPage).catch(err => console.log(err))
    }

    deleteThePassenger = async () => {
        await fetch("/api/passengers/deleteThePassenger/" + this.state.selectedPassengerId, {
            method: 'DELETE'
        }).then(() => {
            const flightInstance = new Flights();
            flightInstance.getAllFlights();
            window.location.reload()
        }).catch (error => {
            console.log(error);
        })
    }

    render() {
        const pages = Array.from({ length: this.state.howManyPages }, (_, index) => index + 1);

        return (
            <div className={"header"}>
                <p className={"flight-header"}>Lista pasażerów lotu o numerze: {this.state.flightNumber}</p>
                <div style={{marginBottom: '2%', float: 'inline-end'}}>
                    <Button onClick={() => this.setState({showManagementPassengersModal: true, mode: 'Dodaj nowego pasażera'})}>Dodaj pasażera</Button>
                </div>
                <Table striped bordered hover variant="light">
                    <thead>
                    <tr style={{textAlign: 'center'}}>
                        <th>ID</th>
                        <th>Imię</th>
                        <th>Nazwisko</th>
                        <th>Numer telefonu</th>
                        <th>Email</th>
                        <th>Akcje</th>
                    </tr>
                    </thead>
                    <tbody>
                    {this.state.passengers.map((passenger, index) => (
                        <tr key={index}>
                            <td>{passenger.idPassenger}</td>
                            <td>{passenger.firstname}</td>
                            <td>{passenger.lastname}</td>
                            <td>{passenger.phoneNumber}</td>
                            <td>{passenger.email}</td>
                            <td className="buttonContainer" style={{textAlign: 'center'}}>
                                <Button style={{marginRight: '2%'}} onClick={() => this.setState({selectedPassengerId: passenger.idPassenger, showManagementPassengersModal: true, mode: 'Edytuj pasażera'})}><AiFillEdit/></Button>
                                <Button onClick={() => {this.setState({showDeletePassengerModal: true,  selectedPassengerId: passenger.idPassenger})}}><HiTrash/></Button>
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


                {this.state.showManagementPassengersModal && (
                    <div className="modal-container">
                        <ManagementPassenger show={this.state.showManagementPassengersModal} closeManagementPassenger={() =>this.setState({showManagementPassengersModal: false, selectedPassengerId: 0})} mode={this.state.mode}
                        passengerToEdit={this.state.passengers.filter(passenger => passenger.idPassenger === this.state.selectedPassengerId)} flight={this.state.idFlight}/>
                    </div>
                )}

                {this.state.showDeletePassengerModal && (
                    <Modal show={this.state.showDeletePassengerModal} onHide={() => this.setState({showDeletePassengerModal: false})} size={"lg"}>
                        <Modal.Header closeButton>
                            <Modal.Title id="example-modal-sizes-title-xl">
                                {this.state.passengers
                                    .filter(pass => pass.idPassenger === this.state.selectedPassengerId)
                                    .map(pass => 'Potwierdzenie usunięcia pasażera: ' + pass.firstname + " " + pass.lastname)}
                            </Modal.Title>
                        </Modal.Header>
                        <Modal.Footer>
                            <Button variant="secondary" onClick={() => this.setState({ showDeletePassengerModal: false })}>Zamknij</Button>
                            <Button variant="primary" onClick={this.deleteThePassenger}>Potwierdź</Button>
                        </Modal.Footer>
                    </Modal>
                )}
            </div>
        )
    }
}
export default Passengers