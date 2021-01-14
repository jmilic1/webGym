import React from "react";
import './addGymOwner.styles.scss'
import CustomButton from "../../components/custom-buttom/custom-button.component";

class AddGymOwner extends React.Component {
   constructor(props) {
      super(props);
      this.state = {
         gyms: [],
         gymId: '0'
      }
   }

   async componentDidMount() {
      if (this.props.role === 'OWNER') {
         await fetch(this.props.backendURL + "myGyms", {
            method: "GET",
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
         }).then(res => {
            if (!res.ok) {
               throw new Error("HTTP Error! " + res.status)
            } else {
               return res.json()
            }
         }).then(gyms => {
            this.setState({
               gyms: gyms
            })
         }
         ).catch(e => {
            alert("Došlo je do pogreške: " + e.message)
         })
      } else if (this.props.role === 'ADMIN') {
         await fetch(this.props.backendURL + "gymList", {
            method: "GET",
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
         }).then(res => {
            if (!res.ok) {
               throw new Error("HTTP Error! " + res.status)
            } else {
               return res.json()
            }
         }).then(gyms => {
            this.setState({
               gyms: gyms
            })
         }
         ).catch(e => {
            alert("Došlo je do pogreške: " + e.message)
         })
      }
   }

   handleChange = event => {
      this.setState({ gymId: event.target.value });
   };

   handleSubmitClick = () => {
      if (this.state.gymId === '0') {
         alert("Vaš izbor nije teretana. Molimo, izaberite teretanu.")
         return;
      }
      fetch(this.props.backendURL + "addGymOwner", {
         method: "POST",
         headers: { 'Content-Type': 'application/json' },
         credentials: 'include',
         body: JSON.stringify({ gymId: this.state.gymId, username: this.props.match.params.username })
      }).then(res => {
         if (!res.ok) {
            if (res.status === 405) {
               throw new Error("Voditelj već vodi odabranu teretanu!")
            } else {
               throw new Error("HTTP Error! " + res.status)
            }
         } else {
            alert("Voditelj uspješno dodan!")
         }
      }).catch(e => {
         alert("Došlo je do pogreške: " + e.message)
      })
   }


   render() {
      return (
         <div className='addOwner-page-container'>
            <div className='addOwner-container'>
               <div className='addOwner-info-container'>
                  {true &&
                     <div className='user-info-form'>
                        <div className='userInfo-formInput'>
                           <label htmlFor='username'>Korisničko ime</label>
                           <input type='text' value={this.props.match.params.username} name='username'
                              disabled />
                        </div>
                        <div className='userInfo-formInput'>
                           <label htmlFor='gym'>Izaberite teretanu</label>
                           <select value={this.state.gymId} onChange={this.handleChange} name='gym'>
                              <option defaultValue value='0'>Izaberite teretanu</option>
                              {this.state.gyms.map(gym => (
                                 <option key={gym.id} value={gym.id}>
                                    {gym.name}
                                 </option>
                              ))}
                           </select>
                        </div>
                     </div>
                  }
               </div>
               <div className='button-container'>
                  <CustomButton onClick={this.handleSubmitClick}>Dodaj</CustomButton>
               </div>
            </div>
         </div>
      );
   }
}

export default AddGymOwner