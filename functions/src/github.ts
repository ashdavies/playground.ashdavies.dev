interface Item {
    name: string
    path: string
    sha: string
    size: string
    url: string
    html_url: string
    git_url: string
    download_url: string
    type: string
    _links: Links
  }
  
  interface Links {
    self: string
    git: string
    html: string
  }