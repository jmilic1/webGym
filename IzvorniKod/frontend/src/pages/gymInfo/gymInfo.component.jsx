import React from "react";
import GymDetailsComponent from "../../components/gymDetails/gymDetails.component";
import './gymInfo.styles.scss'
import MembershipListComponent from "../../components/membershipList/membershipList.component";
import LocationListComponent from "../../components/locationList/locationList.component";
import CoachListComponent from "../../components/coachList/coachList.component";

class GymInfo extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            gym: null
        }
    }

    async componentDidMount() {
        const url = this.props.backendURL + "gymInfo?";


        const params = {id: this.props.match.params.id};
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
                    gym: gym
                })
            }
        ).catch(e => {
            alert("Došlo je do pogreške: " + e.message)
        })
        console.log(this.state.gym)
    }


    render() {
        return (
            <div className = 'gym-page-container'>
                <div className='gym-container'>
                    <div className='gym-details'>
                        {this.state.gym &&
                            <GymDetailsComponent
                                name={this.state.gym.name}
                                description={this.state.gym.description}
                                email={this.state.gym.email}
                                role={this.props.role}/>
                        }
                    </div>
                    <div className='gym-info1'>
                        <h3 id='header'>Članarine</h3>
                        <div className='locations-container'>
                            { this.state.gym?.memberships.length === 0 ?
                                <h5>Ne postoje članarine za odabranu teretanu!</h5>
                                :
                                this.state.gym &&
                                    <MembershipListComponent
                                         memberships={this.state.gym?.memberships}/>

                            }
                        </div>
                    </div>
                    <div className='gym-info2'>
                        <h3 id='header'>Lokacije</h3>
                        <div className='locations-container'>
                        { this.state.gym?.locations.length === 0 ?
                            <h5>Ne postoje lokacije za odabranu teretanu!</h5>
                            :
                            // <p></p>
                            this.state.gym &&
                            <LocationListComponent
                                memberships={this.state.gym?.locations}/>
                        }
                        </div>
                    </div>
                    <div className='gym-info3'>
                        <h3 id='header'>Treneri</h3>
                        <div className='locations-container'>
                            { this.state.gym?.coaches.length === 0 ?
                                <h5>Ne postoje treneri za odabranu teretanu!</h5>
                                :
                                // <p></p>
                                this.state.gym &&
                                <CoachListComponent
                                    memberships={this.state.gym?.coaches}/>
                            }
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default GymInfo