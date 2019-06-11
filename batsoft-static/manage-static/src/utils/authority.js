// use localStorage to store the authority info, which might be sent from server in actual project.
export function getAuthority() {
  return localStorage.getItem('user-authority');
}

export function setAuthority(authority) {
  return localStorage.setItem('user-authority', authority);
}


export function getPermissions() {
  return localStorage.getItem('user-permissions');
}

export function setPermissions(permissions) {
  return localStorage.setItem('user-permissions', permissions);
}

export function hasPermissions(permissions) {
  const userPermissions=localStorage.getItem('user-permissions');
  return userPermissions.includes(permissions);
}
