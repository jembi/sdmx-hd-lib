using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;

using WHO.SDMX.HealthDomain.Library;
using WHO.SDMX.HealthDomain.Library.DOM;
using WHO.SDMX.HealthDomain.Library.Extensions;

namespace Sample1
{
    class Program
    {
        /// <summary>
        /// Sample 1 - Import 3 indicator definitions exported from IMR in order of increasing complexity: simple count, percent, disaggregation
        /// </summary>
        /// <param name="args">None</param>
        static void Main(string[] args)
        {
            try
            {
                #region Initialise

            //  Create new DOM Object to work with
                SDMXDom dom = new SDMXDom();
                dom.OnDOMValidationError += new EventHandler<DOMValidationEventArgs>(dom_OnDOMValidationError);

            //  Create an sdmxFile object to read / write files
                SDMXFile sdmxFile = new SDMXFile();

                const string zipFileLocation = @"../../../../SDMX_Sample_Export.zip";

                #endregion

                #region Load the DOM

            //  Load + Unzip the IMR test data into the sdmFile object. 
            //  This object is used to manage all interaction with the phyical disk.
                sdmxFile.LoadFromZip(zipFileLocation);

            //  Load the DOM + Validate using all files in the SDMXFilesContainer
                dom.Load(sdmxFile.Files, true);

                #endregion

                #region DSD Processing

            //  Process the CodeLists
                Console.WriteLine("The CodeLists from the DSD are being processed \r\n");

                foreach (CodeList codeList in dom.CodeListsDSD)
                {
                    Console.WriteLine("[{0}] :", codeList.Identifier);

                    if (codeList.Codes != null)
                    {
                        foreach (Code code in codeList.Codes)
                        {
                            Console.WriteLine("\t{0}", code);
                        }
                    }
                }

                WriteColorMessage("\r\nPress any key to process the HierarchicalCodeList...", ConsoleColor.Green);
                Console.ReadKey();

            //  Process the HierarchicalCodeList
                foreach (HierarchicalCodeList hierarchicalCodeList in dom.HierarchicalCodeLists)
                {
                    Console.WriteLine("HierarchicalCodeList {0}, is being processed", hierarchicalCodeList.Identifier);

                    foreach (Hierarchy hierarchy in hierarchicalCodeList.Hierarchies)
                    {
                        Console.WriteLine("\tHierarchy {0}, is being processed", hierarchy.ID);
                    }
                }

                WriteColorMessage("\r\nPress any key to process the DSD Concepts...", ConsoleColor.Green);
                Console.ReadKey();

            //  Process the Concepts
                Console.WriteLine("The Concepts from the DSD are being processed \r\n");

                foreach (ConceptScheme conceptScheme in dom.ConceptSchemesDSD)
                {
                    Console.WriteLine("[{0}]", conceptScheme.Identifier);

                    foreach (Concept Concept in conceptScheme.Concepts)
                    {
                        Console.WriteLine("\t{0} : {1}", Concept.Identifier, Concept.Name);
                    }
                }


                WriteColorMessage("\r\nPress any key to process the Observation Data...", ConsoleColor.Green);
                Console.ReadKey();

            //  Process the Observation Data
                Console.WriteLine("The Observation Data are being processed \r\n");

                if (dom.DataSet == null)
                {
                    Console.WriteLine("No observation data found");
                }
                else
                {
                    foreach (Series serie in dom.DataSet.Series)
                    {
                        Console.WriteLine("The Indicator : {0}, is being processed", serie.Indicator.Description.EnglishOrFirst());

                        if (serie.Observations != null && serie.Observations.Count == 0)
                        {
                            Console.WriteLine("The Indicator : {0}, has no data", serie.Indicator.Description);
                        }
                        else
                        {
                            foreach (Observation obs in serie.Observations)
                            {
                                Console.WriteLine("The Indicator : {0}, has an observed data value of {1} for the time period {2}",
                                    serie.Indicator.Description, obs.Value, obs.TimePeriod);
                            }
                        }
                    }
                }

                WriteColorMessage("\r\nPress any key to process the MSD CodeLists...", ConsoleColor.Green);
                Console.ReadKey();

                #endregion

                #region MSD Processing

            //  Process the CodeLists
                Console.WriteLine("The CodeLists from the MSD are being processed \r\n");

                foreach (CodeList codeList in dom.CodeListsMSD)
                {
                    Console.WriteLine("[{0}] :", codeList.Identifier);

                    if (codeList.Codes != null)
                    {
                        foreach (Code code in codeList.Codes)
                        {
                            Console.WriteLine("\t{0}", code);
                        }
                    }
                }

                WriteColorMessage("\r\nPress any key to process the MSD Concepts", ConsoleColor.Green);
                Console.ReadKey();

            //  Process the MSD Concepts
                Console.WriteLine("The Concepts from the MSD are being processed \r\n");

                foreach (ConceptScheme conceptScheme in dom.ConceptSchemesMSD)
                {
                    Console.WriteLine("[{0}]", conceptScheme.Identifier);

                    foreach (Concept Concept in conceptScheme.Concepts)
                    {
                        Console.WriteLine("\t{0} : {1}", Concept.Identifier, Concept.Name);
                    }
                }


                WriteColorMessage("\r\nPress any key to process the MetadataStructures", ConsoleColor.Green);
                Console.ReadKey();

            //  Read the metadata associtated with each code-list
                Console.WriteLine("The MetadataStructures are being processed \r\n");

                foreach (MetadataStructure mds in dom.MetadataStructures)
                {
                //  Get metadatas for TargetIdentifier FTI
                    FullTargetIdentifier fullTargetIdentifier = mds.FullTargetIdentifiers["FTI_WHO"];

                //  FIRST EXAMPLE : GET METADATA FOR A SINGLE IDENTIFIER COMPONENT 
                //  (i.e. Indicator Properties)

                    if (fullTargetIdentifier != null)
                    {
                    //  Get metadata for each identifier components
                        foreach (IdentifierComponent ic in fullTargetIdentifier.IdentifierComponents)
                        {
                        //  Get all AttributeValueSet corresponding to the current IdentifierComponent
                            IEnumerable<AttributeValueSet> metadatas = ic.GetMetadata(fullTargetIdentifier, dom);

                            if (metadatas == null)
                            {
                                Console.WriteLine("No metadata exists for the IdentifierComponent {0}", ic.ID);
                            }
                            else
                            {
                            //  If there is metadata for the current IdentifierCompoment, loop on each attribute value set.
                            //  Here we can identifiy to which Code(s) the metadata correspond (i.e. Indicator 6)

                                foreach (AttributeValueSet attributeValueSet in metadatas)
                                {
                                //  Idenfiy the metadata Code
                                    ComponentValue targetValue = attributeValueSet.TargetValues.First();
                                    Console.WriteLine("Metadatas for {0} {1}", ic.ID, targetValue.Value.Value);

                                    foreach (ReportedAttribute reportedAttribute in attributeValueSet.ReportedAttributes)
                                    {
                                    //  Reported attribute value can be either localized or not (String or LocalizedString)
                                    //  Therefore, we must check the value type before processing it.

                                        if (reportedAttribute.StringValue != null)
                                        {
                                        //  Print the string value
                                            Console.WriteLine("\t{0} : {1}", reportedAttribute.Concept.Name, reportedAttribute.StringValue);
                                        }

                                        if (reportedAttribute.LocalizedStringValue != null )
                                        {
                                        //  Print the string value for each language
                                            foreach (KeyValuePair<string, string> item in reportedAttribute.LocalizedStringValue)
                                            {
                                                Console.WriteLine("\t{0} [{1}] : {2}", reportedAttribute.Concept.Name, item.Key, item.Value);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                //  SECOND EXAMPLE : GET METADATA FOR MULTIPLE IDENTIFIER COMPONENTS
                //  i.e. Indicator > IndicatorSet

                //  Note : In our sdmx-hd file sample, there is not such metadatas so the result will be null.
                    if (fullTargetIdentifier != null)
                    {
                        List<IdentifierComponent> idenfierComponents = new List<IdentifierComponent>()
                        {
                            fullTargetIdentifier.IdentifierComponents["IC_ISET"],
                            fullTargetIdentifier.IdentifierComponents["IC_ISET"],
                            fullTargetIdentifier.IdentifierComponents["IC_INDICATOR"]
                        };
                        IEnumerable<AttributeValueSet> multipleIdenfierComponentsMetadatas = idenfierComponents.GetMetadata(fullTargetIdentifier, dom);
                    }
                }
                #endregion
            }
            catch (SDMXException sdmxex)
            {
                Console.ForegroundColor = ConsoleColor.Red;
                Console.WriteLine(sdmxex.Message);

            }
            catch (Exception ex)
            {
                Console.ForegroundColor = ConsoleColor.Red;
                Console.WriteLine(ex);
            }

            WriteColorMessage("\r\nPress any key to close the program...", ConsoleColor.Blue);
            Console.ReadKey();
        }

        static void dom_OnDOMValidationError(object sender, DOMValidationEventArgs e)
        {
            WriteColorMessage(String.Format("{0} : {1}", e.ErrorID, e.Message), ConsoleColor.Red);
        }

        private static void WriteColorMessage(string message, ConsoleColor color)
        {
            ConsoleColor previousColor = Console.ForegroundColor;
            Console.ForegroundColor = color;
            Console.WriteLine(message);
            Console.ForegroundColor = previousColor;
        }
    }
}
