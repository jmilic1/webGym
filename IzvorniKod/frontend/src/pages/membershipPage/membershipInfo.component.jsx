import React from "react";
import './membershipInfo.styles.scss'

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


        const params = {id: this.props.match.params.id};
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

    // async fetchGymName() {
    //     const url = this.props.backendURL + "gymInfo?";
    //
    //
    //     const params = {id: this.state.membership?.idGym};
    //     const searchParams = new URLSearchParams(params);
    //
    //     await fetch(url + searchParams.toString(), {
    //         method: "GET",
    //         headers: { 'Content-Type': 'application/json' },
    //         credentials: 'include'
    //     }).then(res => {
    //         if (!res.ok) {
    //             throw new Error("HTTP Error! " + res.status)
    //         } else {
    //             return res.json()
    //         }
    //     }).then(gym => {
    //             this.setState({
    //                 gym: gym
    //             })
    //         }
    //     ).catch(e => {
    //         alert("Došlo je do pogreške: " + e.message)
    //     })
    //     console.log(this.state.gym)
    // }


    render() {
        return (
           <div className='membership-page-container'>
               <div className='membership-container'>
                   {this.state.membership &&
                   <div className='user-info-form'>
                       <div className='userInfo-formInput'>
                           <label htmlFor='interval'>Trajanje</label>
                           <input type='text' value={this.state.membership.interval} name='interval'
                                  disabled={!this.state.modifyGymInfo}/>
                       </div>
                       <div className='userInfo-formInput'>
                           <label htmlFor='description'>Opis</label>
                           <input type='text' value={this.state.membership.description} name='description'
                                  disabled={!this.state.modifyGymInfo}/>
                       </div>
                       <div className='userInfo-formInput'>
                           <label htmlFor='price'>Cijena</label>
                           <input type='text' value={this.state.membership.price} name='price'
                                  disabled={!this.state.modifyGymInfo}/>
                       </div>
                   </div>
                   }
               </div>
           </div>
        );
    }
}

export default MembershipInfo