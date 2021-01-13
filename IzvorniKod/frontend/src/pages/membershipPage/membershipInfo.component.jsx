import React from "react";
import './membershipInfo.styles.scss'
import CustomButton from "../../components/custom-buttom/custom-button.component";
import { Link } from 'react-router-dom'

class MembershipInfo extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            membership: null,
            gymName: ''
        }
    }

    async componentDidMount() {
        const url = this.props.backendURL + "membership?";


        const params = { id: this.props.match.params.id };
        const searchParams = new URLSearchParams(params);

        await fetch(url + searchParams.toString(), {
            method: "GET",
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include'
        }).then(res => {
            if (!res.ok) {
                throw new Error("HTTP Error! " + res.status)
            } else {
                return res.json()
            }
        }).then(membership => {
            this.setState({
                membership: membership
            })
        }
        ).catch(e => {
            alert("Došlo je do pogreške: " + e.message)
        })
        console.log(this.state.membership)
        // await this.fetchGymName()
    }

    async fetchGymName() {
        const url = this.props.backendURL + "gymInfo?";
        const params = { id: this.state.membership?.id };
        const searchParams = new URLSearchParams(params);

        await fetch(url + searchParams.toString(), {
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
                gymName: gym.name
            })
        }
        ).catch(e => {
            alert("Došlo je do pogreške: " + e.message)
        })
    }

    // handleButtonJoinClick = () => {
    //     fetch(this.props.backendURL + "buyMembership", {
    //         method: "POST",
    //         headers: { 'Content-Type': 'application/json' },
    //         credentials: 'include',
    //         body: JSON.stringify({ id: this.props.match.params.id, username: this.props.username })
    //     }).then(res => {
    //         if (!res.ok) {
    //             throw new Error("HTTP Error! " + res.status)
    //         } else {
    //             alert("Uspješno ste se učlanili u odabranu teretanu!");
    //         }
    //     }).catch(e => {
    //         alert("Došlo je do pogreške: " + e.message)
    //     })
    // }


    render() {
        return (
            <div className='membership-page-container'>
                <div className='membership-container'>
                    <div className='membership-info-container'>
                        {this.state.membership &&
                            <div className='user-info-form'>
                                {/*<div className='userInfo-formInput'>
                               <label htmlFor='gymName'>Ime teretane</label>
                               <input type='text' value={this.state.gymName} name='gymName'
                                      disabled/>
                           </div>*/}
                                <div className='userInfo-formInput'>
                                    <label htmlFor='interval'>Trajanje</label>
                                    <input type='text' value={this.state.membership.interval} name='interval'
                                        disabled />
                                </div>
                                <div className='userInfo-formInput'>
                                    <label htmlFor='description'>Opis</label>
                                    <input type='text' value={this.state.membership.description} name='description'
                                        disabled />
                                </div>
                                <div className='userInfo-formInput'>
                                    <label htmlFor='price'>Cijena</label>
                                    <input type='text' value={this.state.membership.price} name='price'
                                        disabled />
                                </div>

                            </div>
                        }

                    </div>
                    {this.props.loggedIn && this.props.role === "CLIENT" &&
                        <div className='button-container'>
                            <Link to={{ pathname: '/paymentPage', aboutProps: { id: this.props.id, isPlan: false } }}>
                                <CustomButton>Učlani se</CustomButton>
                            </Link>
                        </div>
                    }
                </div>
            </div>
        );
    }
}

export default MembershipInfo