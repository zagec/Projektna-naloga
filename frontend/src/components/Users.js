import User from './User'

function Users(props) {
    return (
        <div className="p-10">
            {props.users.map((user) => (
                <User key={user._id} user={user}/>
            ))}
        </div>
    )
}

export default Users