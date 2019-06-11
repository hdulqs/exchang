// use localStorage to store the authority info, which might be sent from server in actual project.
export function getGlobData() {
  return localStorage.getItem('system-globData');
}

export function setGlobData(globInfo) {

  return localStorage.setItem('system-globData', JSON.stringify(globInfo));
}
