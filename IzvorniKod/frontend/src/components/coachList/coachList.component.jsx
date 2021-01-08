import React from "react";
import './coachList.styles.scss'
import CoachMetadataContainer from "../coach-metadata-container/coach-metadata-container.component";

class CoachListComponent extends React.Component {
    constructor(){
        super()
    }

    render() {
        return(
            <div className = 'coach-container'>
                <div className = 'coach-header-container'>
                    <h3>Kor. ime</h3>
                    <h3>Ime</h3>
                    <h3>Prezime</h3>
                </div>
                {this.props.coaches.map(coach =>
                    <CoachMetadataContainer
                        country = {coach.username}
                        street = {coach.name}
                        city = {coach.surname}
                        key = {coach.username}/>
                )}
            </div>
        )
    }
}

export default CoachListComponent