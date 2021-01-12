import React from 'react'
import JobRequest from '../../components/jobRequest/jobRequest.component';
import './jobRequests.styles.scss'

class JobRequests extends React.Component {

    constructor() {
        super();
        this.state = {
            requests: []
        }
    }
    
    componentDidMount() {
        fetch(this.props.backendURL + "allJobRequests", {
            method: 'GET',
            credentials: "include"
        }).then(response => {
            if (response.status === 200) {
                return response.json()
            } else {
                return Promise.reject()
            }
        }).then(requests => {
            this.setState({
                requests: requests
            })
        }, function () {
            alert("Došlo je do pogreške")
        })
    }

    render() {
        return (
            <div className='jobRequests-page-container'>
                <div className='jobRequests-header-container'>
                    <p className='font'>
                        Svi neodgovoreni zahtjevi za posao
                    </p>
                </div>

                <div className='allRequests'>
                    {this.state.requests.length === 0 ?
                        <h2>Svi zahtjevi su odgovoreni!</h2>
                        :
                        this.state.requests.map(request =>
                        <JobRequest key={request.id} id={request.id} description={request.description} coach={request.coach} gymName={request.gymName} backendUrl = {this.props.backendURL}/>
                    )}    
                </div>
                
            </div>
        )
    }
}

export default JobRequests