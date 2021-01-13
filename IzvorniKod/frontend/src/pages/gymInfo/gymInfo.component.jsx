import React from "react";
import GymDetailsComponent from "../../components/gymDetails/gymDetails.component";
import './gymInfo.styles.scss'
import MembershipListComponent from "../../components/membershipList/membershipList.component";
import LocationListComponent from "../../components/locationList/locationList.component";
import CoachListComponent from "../../components/coachList/coachList.component";
import CustomButton from "../../components/custom-buttom/custom-button.component";

class GymInfo extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            gym: {},
            addLocation: false,
            newCountry: "",
            newCity: "",
            newStreet: "",
            newOpensAt: "",
            newClosesAt: "",
            newPhoneNumber: "",
            addJobRequest: false,
            locations: [],
            coaches: [],
            memberships: [],
            showAddLocation: props.location.aboutProps ? props.location.aboutProps.showAddLocation : false
        }
    }

    async componentDidMount() {
        const url = this.props.backendURL + "gymInfo?";


        const params = { id: this.props.match.params.id };
        const searchParams = new URLSearchParams(params).toString();

        await fetch(url + searchParams, {
            method: "GET",
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include'
        }).then(res => {
            if (!res.ok) {
                throw new Error("HTTP Error! " + res.status)
            } else {
                return res.json()
            }
        }).then(gym => {
            this.setState({
                gym: gym,
                locations: gym.locations,
                coaches: gym.coaches,
                memberships: gym.memberships,
            })
        }
        ).catch(e => {
            alert("Došlo je do pogreške: " + e.message)
        })
        console.log(this.state.gym)
    }

    handleChangeAddLocationFlag = () => {
        this.setState({
            addLocation: !this.state.addLocation
        })
    }

    handleJobRequestFlag = () => {
        this.setState({
            addJobRequest: !this.state.addJobRequest
        })
    }

    handleChange = event => {
        const { value, name } = event.target;

        this.setState({ [name]: value });
    };

    handleNewLocationSubmit = () => {
        const body = {
            id: this.state.gym.id, country: this.state.newCountry, city: this.state.newCity,
            street: this.state.newStreet, opensAt: this.state.newOpensAt, closesAt: this.state.newClosesAt,
            phoneNumber: this.state.newPhoneNumber
        }
        fetch(this.props.backendURL + "gymInfo", {
            method: 'POST',
            credentials: 'include',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        }).then(response => {
            if (response.ok) {

                const newArray = this.state.gym.locations
                newArray.push(body)
                this.setState({
                    locations: newArray,
                    newCountry: "",
                    newCity: "",
                    newStreet: "",
                    newOpensAt: "",
                    newClosesAt: "",
                    newPhoneNumber: "",
                    addLocation: false
                })
            } else {
                throw new Error("HTTP Error! " + response.status)
            }
        }).catch(e => alert("Došlo je do pogreške: " + e.message))

    }

    handleJobRequestSubmit = () => {
        fetch(this.props.backendURL + "jobRequest", {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify({ gymId: this.state.gym.id, description: this.state.newJobDescription })
        }).then(response => {
            if (response.ok) {
                this.setState({
                    newJobDescription: "",
                    addJobRequest: false
                })
            } else {
                throw new Error("HTTP Error! " + response.status)
            }
        }).catch(e => {
            alert("Došlo je do pogreške: " + e.message)
        })
    }

    render() {
        return (
            <div className='gym-page-container'>
                <div className='gym-container'>
                    <div className='gym-details'>
                        {this.state.gym &&
                            <GymDetailsComponent
                                id={this.state.gym.id}
                                name={this.state.gym.name}
                                description={this.state.gym.description}
                                email={this.state.gym.email}
                                role={this.props.role}
                                backendUrl={this.props.backendURL} />
                        }
                        {this.props.role === "COACH" ?
                            <CustomButton onClick={this.handleJobRequestFlag}> Zamolba za posao </CustomButton>
                            :
                            <div />
                        }
                        {this.state.addJobRequest ?
                            <div className='grid-container-edo'>
                                <div>
                                    <label>Opis:</label> <br />
                                    <textarea className="textarea" name='newJobDescription' value={this.state.newJobDescription} onChange={this.handleChange} required />
                                </div>
                                <div className='jobRequest-buttons'>
                                    <button onClick={this.handleJobRequestSubmit}>Pošalji</button>
                                    <button onClick={this.handleJobRequestFlag}>Otkaži</button>
                                </div>

                            </div>
                            :
                            <div />
                        }
                    </div>

                    <div>
                        <div className='gym-info1'>
                            <h3 id='header'>Članarine</h3>
                            <div className='locations-container'>
                                {this.state.memberships.length === 0 ?
                                    <h5>Ne postoje članarine za odabranu teretanu!</h5>
                                    :
                                    this.state.gym &&
                                    <MembershipListComponent
                                        memberships={this.state.memberships} />

                                }
                            </div>
                        </div>

                        <div className='gym-info2'>
                            <h3 id='header'>Lokacije</h3>
                            <div className='locations-container'>
                                {this.state.locations.length === 0 ?
                                    <h5>Ne postoje lokacije za odabranu teretanu!</h5>
                                    :
                                    // <p></p>
                                    this.state.gym &&
                                    <LocationListComponent
                                        locations={this.state.locations} locationComplex={this.state.showAddLocation} />
                                }

                                <div className='align'>
                                    {this.props.role === "OWNER" && this.state.showAddLocation ?
                                        this.state.addLocation ?
                                            <CustomButton onClick={this.handleChangeAddLocationFlag}> Otkaži </CustomButton>
                                            :
                                            <CustomButton onClick={this.handleChangeAddLocationFlag}> Dodaj lokaciju </CustomButton>
                                        :
                                        <div />
                                    }

                                    {this.state.addLocation ?
                                        <div className='grid-containerLoc'>
                                            <div className='item1Loc'>
                                                <label>Država: </label>
                                                <input type="text" name='newCountry' value={this.state.newCountry} onChange={this.handleChange} required />
                                            </div>

                                            <div className='item2Loc'>
                                                <label>Grad: </label>
                                                <input type="text" name='newCity' value={this.state.newCity} onChange={this.handleChange} required />
                                            </div>

                                            <div className='item3Loc'>
                                                <label>Ulica: </label>
                                                <input type="text" name='newStreet' value={this.state.newStreet} onChange={this.handleChange} required />
                                            </div>

                                            <div className='item4Loc'>
                                                <label>Broj telefona: </label>
                                                <input type="text" name='newPhoneNumber' value={this.state.newPhoneNumber} onChange={this.handleChange} minLength="9" maxLength="9" required />
                                            </div>

                                            <div className='item5Loc'>
                                                <label>Od: </label>
                                                <input type="time" name='newOpensAt' value={this.state.newOpensAt} onChange={this.handleChange} required />
                                            </div>

                                            <div className='item6Loc'>
                                                <label>Do: </label>
                                                <input type="time" name='newClosesAt' value={this.state.newClosesAt} onChange={this.handleChange} required />
                                            </div>

                                            <div className='item7Loc'>
                                                <br />
                                                <button onClick={this.handleNewLocationSubmit}>Dodaj</button>
                                            </div>

                                        </div>
                                        :
                                        <div />
                                    }
                                </div>
                            </div>
                        </div>
                        <div className='gym-info3'>
                            <h3 id='header'>Treneri</h3>
                            <div className='locations-container'>
                                {this.state.coaches.length === 0 ?
                                    <h5>Ne postoje treneri za odabranu teretanu!</h5>
                                    :
                                    // <p></p>
                                    this.state.gym &&
                                    <CoachListComponent
                                        coaches={this.state.coaches} />
                                }
                            </div>
                        </div>
                    </div>


                </div>
            </div>
        );
    }
}

export default GymInfo