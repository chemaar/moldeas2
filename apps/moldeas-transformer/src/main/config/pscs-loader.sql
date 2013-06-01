delete  from DB.DBA.load_list;
sparql clear graph <http://purl.org/weso/pscs/cpv>;
ld_dir ('/tmp/pscs/cpv/', '*.ttl', 'http://purl.org/weso/pscs/cpv');
rdf_loader_run ();

delete  from DB.DBA.load_list;
sparql clear graph <http://purl.org/weso/pscs>;
ld_dir ('/tmp/pscs/', '*.ttl', 'http://purl.org/weso/pscs');
rdf_loader_run ();



delete  from DB.DBA.load_list;
sparql clear graph <http://purl.org/weso/pscs/cn/2012>;
ld_dir ('/tmp/pscs/cn/2012', '*.ttl', 'http://purl.org/weso/pscs/cn/2012');
rdf_loader_run ();

delete  from DB.DBA.load_list;
sparql clear graph <http://purl.org/weso/pscs/cpa/2008>;
ld_dir ('/tmp/pscs/cpa/2008', '*.ttl', 'http://purl.org/weso/pscs/cpa/2008');
rdf_loader_run ();



delete  from DB.DBA.load_list;
sparql clear graph <http://purl.org/weso/pscs/cpc/2008>;
ld_dir ('/tmp/pscs/cpc/2008', '*.ttl', 'http://purl.org/weso/pscs/cpc/2008');
rdf_loader_run ();


delete  from DB.DBA.load_list;
sparql clear graph <http://purl.org/weso/pscs/cpv/2008>;
ld_dir ('/tmp/pscs/cpv/2008', '*.ttl', 'http://purl.org/weso/pscs/cpv/2008');
rdf_loader_run ();


delete  from DB.DBA.load_list;
sparql clear graph <http://purl.org/weso/pscs/cpv/2003>;
ld_dir ('/tmp/pscs/cpv/2003', '*.ttl', 'http://purl.org/weso/pscs/cpv/2003');
rdf_loader_run ();


delete  from DB.DBA.load_list;
sparql clear graph <http://purl.org/weso/pscs/isic/v4>;
ld_dir ('/tmp/pscs/isic/v4', '*.ttl', 'http://purl.org/weso/pscs/isic/v4');
rdf_loader_run ();

delete  from DB.DBA.load_list;
sparql clear graph <http://purl.org/weso/pscs/naics/2007>;
ld_dir ('/tmp/pscs/naics/2007', '*.ttl', 'http://purl.org/weso/pscs/naics/2007');
rdf_loader_run ();


delete  from DB.DBA.load_list;
sparql clear graph <http://purl.org/weso/pscs/naics/2012>;
ld_dir ('/tmp/pscs/naics/2012', '*.ttl', 'http://purl.org/weso/pscs/naics/2012');
rdf_loader_run ();


delete  from DB.DBA.load_list;
sparql clear graph <http://purl.org/weso/pscs/sitc/v4>;
ld_dir ('/tmp/pscs/sitc/v4', '*.ttl', 'http://purl.org/weso/pscs/sitc/v4');
rdf_loader_run ();



