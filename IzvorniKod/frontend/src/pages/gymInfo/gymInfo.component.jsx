import React from "react";
import GymDetailsComponent from "../../components/gymDetails/gymDetails.component";

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
    }


    render() {
        return (
            <div>
                {this.state.gym &&
                    <GymDetailsComponent name={this.state.gym.name} description={this.state.gym.description} email={this.state.gym.email}></GymDetailsComponent>
                }
            </div>
        );
    }
}

export default GymInfo