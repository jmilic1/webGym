import React from 'react'
import './jobRequest.styles.scss'
import { Checkmark } from 'react-checkmark'
import { Icon, InlineIcon } from '@iconify/react';
import timesCircle from '@iconify-icons/fa/times-circle';

class JobRequest extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            view: "EMPTY"
        }
    }
        
    handleRequestTrueSubmit = () => {
        fetch(this.props.backendUrl + "allJobRequests" , {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify({reqId: this.props.id, response: true})
        }).then(response => {
            if (response.ok) {
                this.setState({
                    view: "ACCEPTED"
                })
            } else {
                throw new Error("HTTP Error! " + response.status)
            }
        }).catch(e => {
            alert("Došlo je do pogreške: " + e.message)
        })
    }

    handleRequestFalseSubmit = () => {
        fetch(this.props.backendUrl + "allJobRequests" , {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify({reqId: this.props.id, response: false})
        }).then(response => {
            if (response.ok) {
                this.setState({
                    view: "DENIED"
                })
            } else {
                throw new Error("HTTP Error! " + response.status)
            }
        }).catch(e => {
            alert("Došlo je do pogreške: " + e.message)
        })
    }

    render() {
        return(
            <div className='grid-containerJob'>
            
                <div className='item1Job'>
                    <p>Ime: {this.props.coach.name}</p>
                </div>

                <div className="item3Job">
                    <p>Prezime: {this.props.coach.surname}</p>
                </div>

                <div className="item2Job">
                    <p>Teretana: {this.props.gymName}</p>
                </div>

                <div className="item4Job">
                    <p>Zamolba: <br/> {this.props.description}</p>
                </div>

                <div className="item5Job">
                    <p>
                        <button className="btnJob" onClick={this.handleRequestTrueSubmit}>Prihvati</button>
                        <button onClick={this.handleRequestFalseSubmit}>Odbij</button>
                    </p>
                </div>

                <div className='item6Job'>
                    <p>
                        {this.state.view === "EMPTY" ?
                            <div className='item6Job'></div>
                            :
                            this.state.view === "ACCEPTED" ?
                                <Checkmark size='24px' />
                                :
                                <Icon icon={timesCircle} color="red" width="24" height="24" />
                        }
                        
                        
                    </p>
                </div>
                
            </div>
        )
    }   
}

export default JobRequest