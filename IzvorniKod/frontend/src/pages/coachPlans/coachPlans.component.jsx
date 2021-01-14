import React from 'react'
import './coachPlans.styles.css'
import CoachPlan from "../../components/coachPlan/coachPlan.component";
import BtnAdd from '../../assets/btn_add1.svg'

class CoachPlans extends React.Component {

    constructor() {
        super();
        this.state = {
            dietPlans: [],
            workoutPlans: [],
            showDietPlans: true,
            addNewPlan: false,
            newPlanDescription: "",
            newPlanDateFrom: "",
            newPlanDateTo: "",
            newPlanPrice: ""
        }
    }

    componentDidMount() {
        fetch(this.props.backendURL + "getDietPlans", {
            method: 'GET',
            credentials: "include"
        }).then(response => {
            if (response.status === 200) {
                return response.json()
            } else {
                return Promise.reject()
            }
        }).then(dietPlans => {
            this.setState({
                dietPlans: dietPlans
            })
        }, function () {
            alert("Došlo je do pogreške")
        })

        fetch(this.props.backendURL + "getWorkoutPlans", {
            method: 'GET',
            credentials: "include"
        }).then(response => {
            if (response.status === 200) {
                return response.json()
            } else {
                return Promise.reject()
            }
        }).then(plans => {
            this.setState({
                workoutPlans: plans
            })
        }, function () {
            alert("Došlo je do pogreške")
        })
    }

    refreshDietPlans = () => {
        fetch(this.props.backendURL + "getDietPlans", {
            method: 'GET',
            credentials: "include"
        }).then(response => {
            if (response.status === 200) {
                return response.json()
            } else {
                return Promise.reject()
            }
        }).then(dietPlans => {
            this.setState({
                dietPlans: dietPlans
            })
        }, function () {
            alert("Došlo je do pogreške")
        })
    }

    refreshWorkoutPlans = () => {
        fetch(this.props.backendURL + "getWorkoutPlans", {
            method: 'GET',
            credentials: "include"
        }).then(response => {
            if (response.status === 200) {
                return response.json()
            } else {
                return Promise.reject()
            }
        }).then(plans => {
            this.setState({
                workoutPlans: plans
            })
        }, function () {
            alert("Došlo je do pogreške")
        })
    }

    setShowDietPlans = () => {
        this.setState({
            showDietPlans: true
        })
    }
    removeShowDietPlans = () => {
        this.setState({
            showDietPlans: false
        })
    }

    handleChange = event => {
        const { value, name } = event.target;

        this.setState({ [name]: value });
    };

    handleChangeAddNewPlanFlag = () => {
        this.setState({
            addNewPlan: !this.state.addNewPlan
        })
    }

    handleBtnSaveNewPlanClick = () => {
        const body = {
            description: this.state.newPlanDescription, dateFrom: this.state.newPlanDateFrom,
            dateTo: this.state.newPlanDateTo, price: this.state.newPlanPrice, isTraining: !this.state.showDietPlans
        }
        fetch(this.props.backendURL + "addPlan", {
            method: 'POST',
            credentials: 'include',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        }).then(response => {
            if (response.ok) {
                if (this.state.showDietPlans) {
                    this.refreshDietPlans()
                    this.setState({
                        addNewPlan: false
                    })
                } else {
                    this.refreshWorkoutPlans()
                    this.setState({
                        addNewPlan: false
                    })
                }
            } else {
                throw new Error("HTTP Error! " + response.status)
            }
        }).catch(e => alert("Došlo je do pogreške: " + e.message))

        // this.state.showDietPlans ? this.refreshDietPlans() : this.refreshWorkoutPlans()
    }

    render() {
        return (
            <div className='coachPlans-page-container'>


                <div className='coachPlans-plansHeaderBtn'>
                    <div className='coachPlans-plansHeader'>
                        <div className={`${this.state.showDietPlans ? "marked" : ''}`} onClick={this.setShowDietPlans}><p>Planovi prehrane</p></div>
                        <div style={{ textAlign: "right" }} className={`${!this.state.showDietPlans ? "marked" : ''}`} onClick={this.removeShowDietPlans}><p>Planovi treninga</p></div>
                    </div>
                    {this.state.addNewPlan ?
                        <div className='btnAdd-container'>

                        </div>
                        :
                        <div className='btnAdd-container'>
                            <button className='btn1' onClick={this.handleChangeAddNewPlanFlag}>
                                <img src={BtnAdd} width='20px' alt='add btn' /> Dodaj plan
                            </button>
                        </div>
                    }
                </div>

                {this.state.addNewPlan ?
                    <div className='grid-containerCPlans'>
                        <div className="item1CPlans">
                            <label>Opis:</label>
                            <br></br>
                            <textarea className="textareaCPlans" name='newPlanDescription' value={this.state.newPlanDescription} onChange={this.handleChange} required />
                        </div>

                        <div className="item2CPlans">
                            <label>Od: </label>
                            <input type="date" onChange={(event) => this.setState({ newPlanDateFrom: event.target.value })} />
                        </div>

                        <div className="item3CPlans">
                            <label>Do: </label>
                            <input type="date" onChange={(event) => this.setState({ newPlanDateTo: event.target.value })} />
                        </div>

                        <div className="item4CPlans">
                            <label>Cijena(kn): </label>
                            <input type='number' min='0' name='newPlanPrice' value={this.state.newPlanPrice} onChange={this.handleChange} />
                        </div>

                        <div className="item5CPlans">
                            <p>
                                <button className="btn" onClick={this.handleChangeAddNewPlanFlag}>Otkaži</button>
                                <button onClick={this.handleBtnSaveNewPlanClick}> Spremi</button>
                            </p>
                        </div>
                    </div>
                    :
                    <div>

                    </div>
                }

                {this.state.showDietPlans ?
                    (<div className='coachPlans-dietPlans'>
                        {this.state.dietPlans.map(plan =>
                            <CoachPlan refreshPlans={this.refreshDietPlans} key={plan.id} id={plan.id} description={plan.description} dateFrom={plan.dateFrom} dateTo={plan.dateTo} price={plan.price} backendUrl={this.props.backendURL} />
                        )}
                    </div>)
                    :
                    (<div className='coachPlans-dietPlans'>
                        {this.state.workoutPlans.map(plan =>
                            <CoachPlan refreshPlans={this.refreshWorkoutPlans} key={plan.id} id={plan.id} description={plan.description} dateFrom={plan.dateFrom} dateTo={plan.dateTo} price={plan.price} backendUrl={this.props.backendURL} />
                        )}
                    </div>)
                }
            </div>
        )

    }
}

export default CoachPlans